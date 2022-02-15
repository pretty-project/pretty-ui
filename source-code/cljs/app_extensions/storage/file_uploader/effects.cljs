
(ns app-extensions.storage.file-uploader.effects
    (:require [app-fruits.dom :as dom]
              [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.file-uploader.engine  :as engine]
              [app-extensions.storage.file-uploader.events  :as events]
              [app-extensions.storage.file-uploader.queries :as queries]
              [app-extensions.storage.file-uploader.subs    :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/load-uploader!
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :destination-id (string)}
  ;
  ; @usage
  ;  [:storage.file-uploader/load-uploader! {...}]
  ;
  ; @usage
  ;  [:storage.file-uploader/load-uploader! :my-uploader {...}]
  ;
  ; @usage
  ;  [:storage.file-uploader/load-uploader! {:allowed-extensions ["htm" "html" "xml"]
  ;                                          :destination-id "my-directory"}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ uploader-id uploader-props]]
      ; - Az egyes fájlfeltöltési folyamatok a fájlfeltöltő ablak bezáródása után még a fájl(ok)
      ;   méretétől függően NEM azonnal fejeződnek be.
      ; - Az uploader-id egyedi azonosító alkalmazása lehetővé teszi, hogy az egy időben történő
      ;   különböző fájlfeltöltések kezelhetők legyenek.
      ; - A request-id azonosító feltöltési folyamatonként eltérő kell legyen, ehhez szükséges,
      ;   hogy az uploader-id azonosító is ... eltérő legyen!
      {:db (r events/store-uploader-props! db uploader-id uploader-props)
       :fx [:storage.file-uploader/open-file-selector! uploader-id uploader-props]}))

(a/reg-event-fx
  :storage.file-uploader/cancel-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      {:db (r events/clean-uploader! db uploader-id)
       :dispatch [:ui/close-popup! :storage.file-uploader/view]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/start-progress!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      (let [query     (r queries/get-upload-files-query db uploader-id)
            form-data (r subs/get-form-data             db uploader-id)]
           {:dispatch-n [[:storage.file-uploader/progress-started uploader-id]
                         [:sync/send-query! (engine/request-id uploader-id)
                                            {:body       (dom/merge-to-form-data! form-data {:query query})
                                             :on-success [:storage.file-uploader/progress-successed uploader-id]
                                             :on-failure [:storage.file-uploader/progress-failured  uploader-id]}]]})))

(a/reg-event-fx
  :storage.file-uploader/files-selected-to-upload
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      ; A storage--file-selector input on-change eseménye indítja el a feltöltés inicializálását.
      (if-let [any-file-selected? (engine/any-file-selected?)]
              {:db (r events/init-uploader! db uploader-id)
               :dispatch [:storage.file-uploader/render-uploader! uploader-id]})))

(a/reg-event-fx
  :storage.file-uploader/progress-started
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      {:dispatch-n [[:storage.file-uploader/render-progress-notification! uploader-id]
                    [:ui/close-popup! :storage.file-uploader/view]]}))

(a/reg-event-fx
  :storage.file-uploader/progress-successed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      {; XXX#5087
       ; Az egy feltöltési folyamatok befejezése/megszakadása után késleltetve zárja le az adott feltöltőt
       :dispatch-later [{:ms 3000 :dispatch [:storage.file-uploader/end-uploader! uploader-id]}]
       :dispatch [:item-browser/reload-items! :storage :media]}))

(a/reg-event-fx
  :storage.file-uploader/progress-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      {; XXX#5087
       :dispatch-later [{:ms 3000 :dispatch [:storage.file-uploader/end-uploader! uploader-id]}]}))

(a/reg-event-fx
  :storage.file-uploader/end-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      {:dispatch-later [; A feltöltő lezárása után késleltetve törli ki annak adatait, hogy a még
                        ; látszódó folyamatjelző számára elérhetők maradjanak az adatok.
                        {:ms 500 :dispatch [:storage.file-uploader/clean-uploader! uploader-id]}]
       :dispatch-if [(not (r subs/file-upload-in-progress? db))
                     ; Ha a felöltő lezárásakor nincs aktív feltöltési folyamat, akkor bezárja
                     ; a folyamatjelzőt.
                     ; Az utolsó feltöltési folyamat befejeződése és az utolsó feltöltő lezárása
                     ; közötti időben is indítható új feltöltési folyamat!
                     [:ui/pop-bubble! :storage.file-uploader/progress-notification]]}))