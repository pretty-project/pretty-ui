
(ns app-extensions.storage.file-uploader.events
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :refer [dissoc-in]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [app-extensions.storage.file-uploader.engine  :as engine]
              [app-extensions.storage.file-uploader.queries :as queries]
              [app-extensions.storage.file-uploader.subs    :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-uploader-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id uploader-props]]
  (assoc-in db [:storage :file-uploader/meta-items uploader-id] uploader-props))

(defn init-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [file-selector (dom/get-element-by-id "storage--file-selector")
        files-meta    (dom/file-selector->files-meta file-selector)
        files-data    (dom/file-selector->files-data file-selector)]
       (-> db (assoc-in  [:storage :file-uploader/data-items uploader-id] files-data)
              (update-in [:storage :file-uploader/meta-items uploader-id] merge files-meta))))

(defn clean-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (-> db (dissoc-in [:storage :file-uploader/data-items uploader-id])
         (dissoc-in [:storage :file-uploader/meta-items uploader-id])))

(a/reg-event-db :storage/clean-file-uploader! clean-file-uploader!)

(defn cancel-file-upload!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id file-dex]]
  ; A feltöltendő fájlok adatai közül nem kerülnek törlésre a visszavont fájlok adatai,
  ; mert a változó elemszám miatt a feltöltendő fájlok listája újra renderelődne!
  (let [filesize (get-in db [:storage :file-uploader/data-items uploader-id file-dex :filesize])]
       (-> db (assoc-in  [:storage :file-uploader/data-items uploader-id file-dex :cancelled?] true)
              (update-in [:storage :file-uploader/meta-items uploader-id :files-size] - filesize)
              (update-in [:storage :file-uploader/meta-items uploader-id :file-count]   dec))))

(a/reg-event-db :storage/cancel-file-upload! cancel-file-upload!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/start-file-uploader-progress!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      (let [query     (r queries/get-upload-files-query db uploader-id)
            form-data (r subs/get-form-data             db uploader-id)]
           {:dispatch-n [[:storage/->file-uploader-progress-started uploader-id]
                         [:sync/send-query! (engine/request-id uploader-id)
                                            {:body       (dom/merge-to-form-data! form-data {:query query})
                                             :on-success [:storage/->file-uploader-progress-ended uploader-id]
                                             :on-failure [:storage/->file-uploader-progress-ended uploader-id]}]]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/->files-selected-to-upload
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      ; A storage--file-selector input on-change eseménye indítja el a feltöltés inicializálását.
      (if-let [any-file-selected? (engine/any-file-selected?)]
              {:db (r init-file-uploader! db uploader-id)
               :dispatch [:storage/render-file-uploader! uploader-id]})))

(a/reg-event-fx
  :storage/->file-uploader-progress-started
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      {:dispatch-n [[:ui/close-popup! (engine/popup-id uploader-id)]
                    [:storage/render-file-uploader-progress-notification! uploader-id]]}))

(a/reg-event-fx
  :storage/->file-uploader-progress-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      ; Az egy feltöltési folyamatok befejezése/megszakadása után késleltetve zárja le az adott feltöltőt
      {:dispatch-later [{:ms 3000 :dispatch [:storage/end-file-uploader! uploader-id]}]
       :dispatch [:item-lister/reload-lister! :storage :media]}))

(a/reg-event-fx
  :storage/end-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      {:dispatch-later [; A feltöltő lezárása után késleltetve törli ki annak adatait, hogy a még
                        ; látszódó folyamatjelző számára elérhetők maradjanak az adatok.
                        {:ms 500 :dispatch [:storage/clean-file-uploader! uploader-id]}]
       :dispatch-n [(if-not ; Ha a felöltő lezárásakor nincs aktív feltöltési folyamat, akkor bezárja
                            ; a folyamatjelzőt.
                            ; Az utolsó feltöltési folyamat befejeződése és az utolsó feltöltő lezárása
                            ; közötti időben is indítható új feltöltési folyamat!
                            (r subs/file-upload-in-progress? db)
                            [:ui/pop-bubble! :storage/file-uploader-progress-notification])]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/cancel-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      {:db (r clean-file-uploader! db uploader-id)
       :dispatch [:ui/close-popup! (engine/popup-id uploader-id)]}))

(a/reg-event-fx
  :storage/abort-file-uploader-progress!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]))
       ; TEMP
       ; Ha már elindult a folyamat, akkor meg kell szakítani a feltöltést !

(a/reg-event-fx
  :storage/load-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :destination-id (string)}
  ;
  ; @usage
  ;  [:storage/load-file-uploader! {...}]
  ;
  ; @usage
  ;  [:storage/load-file-uploader! :my-uploader {...}]
  ;
  ; @usage
  ;  [:storage/load-file-uploader! {:allowed-extensions ["htm" "html" "xml"]
  ;                                 :destination-id "..."}]
  (fn [{:keys [db]} event-vector]
      ; - Az egyes fájlfeltöltési folyamatok a fájlfeltöltő ablak bezáródása után még a fájl(ok)
      ;   méretétől függően nem azonnal fejeződnek be.
      ; - Az uploader-id egyedi azonosító alkalmazása lehetővé teszi, hogy az egy időben történő
      ;   különböző fájlfeltöltések kezelhetők legyenek.
      ; - A request-id azonosító feltöltési folyamatonként eltérő kell legyen, ehhez szükséges,
      ;   hogy az uploader-id azonosító is ... eltérő legyen!
      (let [uploader-id    (a/event-vector->second-id   event-vector)
            uploader-props (a/event-vector->first-props event-vector)]
           {:db (r store-uploader-props! db uploader-id uploader-props)
            :storage/open-file-selector! [uploader-id uploader-props]})))
