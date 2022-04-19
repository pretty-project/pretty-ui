
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.effects
    (:require [dom.api                                       :as dom]
              [extensions.storage.file-uploader.events       :as file-uploader.events]
              [extensions.storage.file-uploader.helpers      :as file-uploader.helpers]
              [extensions.storage.file-uploader.queries      :as file-uploader.queries]
              [extensions.storage.file-uploader.side-effects :as file-uploader.side-effects]
              [extensions.storage.file-uploader.validators   :as file-uploader.validators]
              [extensions.storage.file-uploader.subs         :as file-uploader.subs]
              [extensions.storage.file-uploader.views        :as file-uploader.views]
              [plugins.item-browser.api                      :as item-browser]
              [x.app-core.api                                :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/load-uploader!
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :browser-id (keyword)
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
      {:db (r file-uploader.events/store-uploader-props! db uploader-id uploader-props)
       :fx [:storage.file-uploader/open-file-selector! uploader-id uploader-props]}))

(a/reg-event-fx
  :storage.file-uploader/cancel-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      {:db       (r file-uploader.events/clean-uploader! db uploader-id)
       :dispatch [:ui/close-popup! :storage.file-uploader/view]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/start-progress!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      (let [query        (r file-uploader.queries/get-upload-files-query          db uploader-id)
            form-data    (r file-uploader.subs/get-form-data                      db uploader-id)
            validator-f #(r file-uploader.validators/upload-files-response-valid? db uploader-id %)]
           {:dispatch-n [[:storage.file-uploader/progress-started uploader-id]
                         [:sync/send-query! (file-uploader.helpers/request-id uploader-id)
                                            {:body       (dom/merge-to-form-data! form-data {:query query})
                                             :on-success [:storage.file-uploader/progress-successed uploader-id]
                                             :on-failure [:storage.file-uploader/progress-failured  uploader-id]
                                             :validator-f validator-f}]]})))

(a/reg-event-fx
  :storage.file-uploader/files-selected-to-upload
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      ; A storage--file-selector input on-change eseménye indítja el a feltöltés inicializálását.
      (if-let [any-file-selected? (file-uploader.side-effects/any-file-selected?)]
              {:db       (r file-uploader.events/init-uploader! db uploader-id)
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
  (fn [{:keys [db]} [_ uploader-id]]
      ; - XXX#5087
      ;   Az egyes feltöltési folyamatok befejezése/megszakadása után késleltetve zárja le az adott
      ;   feltöltőt, így a felhasználónak van ideje észlelni a visszajelzést.
      ; - Ha a sikeres fájlfeltöltés után még a célmappa az aktuálisan böngészett elem,
      ;   akkor újratölti a listaelemeket.
      (let [browser-id     (get-in db [:storage :file-uploader/meta-items uploader-id :browser-id])
            destination-id (get-in db [:storage :file-uploader/meta-items uploader-id :destination-id])]
           {:dispatch-later [{:ms 8000 :dispatch [:storage.file-uploader/end-uploader! uploader-id]}]
            :dispatch-if    [(r item-browser/browsing-item? db browser-id destination-id)
                             [:item-browser/reload-items! browser-id]]})))

(a/reg-event-fx
  :storage.file-uploader/progress-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      {; XXX#5087
       :dispatch-later [{:ms 8000 :dispatch [:storage.file-uploader/end-uploader! uploader-id]}]}))

(a/reg-event-fx
  :storage.file-uploader/end-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      ; - A feltöltő lezárása után késleltetve törli ki annak adatait, hogy a még
      ;   látszódó folyamatjelző számára elérhetők maradjanak az adatok.
      ; - Ha a felöltő lezárásakor nincs aktív feltöltési folyamat, akkor bezárja a folyamatjelzőt.
      ;   (Az utolsó feltöltési folyamat befejeződése és az utolsó feltöltő lezárása
      ;    közötti időben is indítható új feltöltési folyamat!)
      {:dispatch-later [{:ms 500 :dispatch [:storage.file-uploader/clean-uploader! uploader-id]}]
       :dispatch-if [(not (r file-uploader.subs/file-upload-in-progress? db))
                     [:ui/pop-bubble! :storage.file-uploader/progress-notification]]}))

(a/reg-event-fx
  :storage.file-uploader/render-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:ui/add-popup! :storage.file-uploader/view
                      {:body   [file-uploader.views/body   uploader-id]
                       :header [file-uploader.views/header uploader-id]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/render-progress-notification!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/blow-bubble! :storage.file-uploader/progress-notification
                    {:body #'file-uploader.views/progress-notification-body
                     :autopop? false :user-close? false}])
