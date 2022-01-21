
(ns app-extensions.storage.file-uploader.events
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :refer [dissoc-in]]
              [mid-fruits.random  :as random]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [app-extensions.storage.file-uploader.engine  :as engine]
              [app-extensions.storage.file-uploader.queries :as queries]
              [app-extensions.storage.file-uploader.subs    :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (-> db (dissoc-in [:storage :file-uploader/data-items uploader-id])
         (dissoc-in [:storage :file-uploader/meta-items uploader-id])))

(a/reg-event-db :storage/clean-file-uploader! clean-file-uploader!)

(defn abort-file-upload!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id file-dex]]
  ; A feltöltendő fájlok adatai közül nem kerülnek törlésre a visszavont fájlok adatai,
  ; mert a változó elemszám miatt a feltöltendő fájlok listája újra renderelődne
  (let [filesize (get-in db [:storage :file-uploader/data-items uploader-id file-dex :filesize])]
       (-> db (assoc-in  [:storage :file-uploader/data-items uploader-id file-dex :aborted?] true)
              (update-in [:storage :file-uploader/meta-items uploader-id :files-size] - filesize)
              (update-in [:storage :file-uploader/meta-items uploader-id :file-count] dec))))

(a/reg-event-db :storage/abort-file-upload! abort-file-upload!)

(defn store-uploading-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [file-selector (dom/get-element-by-id "storage--file-selector")
        files-meta    (dom/file-selector->files-meta file-selector)
        files-data    (dom/file-selector->files-data file-selector)]
       (-> db (assoc-in  [:storage :file-uploader/data-items uploader-id] files-data)
              (update-in [:storage :file-uploader/meta-items uploader-id] merge files-meta))))

(defn store-uploader-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id uploader-props]]
  (assoc-in db [:storage :file-uploader/meta-items uploader-id] uploader-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/upload-files!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      (let [query     (r queries/get-upload-files-query db uploader-id)
            form-data (r subs/get-form-data             db uploader-id)]
           {:dispatch-n [[:storage/->file-uploading-in-progress uploader-id]
                         [:sync/send-query! (keyword/add-namespace :storage uploader-id)
                                            {:body       (dom/merge-to-form-data! form-data {:query query})
                                             :on-success [:storage/->files-uploaded uploader-id]
                                             :on-failure [:storage/->files-uploaded uploader-id]}]]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/->files-uploaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      {:dispatch-later [{:ms 3000 :dispatch [:ui/pop-bubble! (keyword/add-namespace :storage uploader-id)]}
                        {:ms 3000 :dispatch [:storage/clean-file-uploader! uploader-id]}]}))

(a/reg-event-fx
  :storage/->file-uploading-in-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:storage/render-file-uploading-progress-notification! uploader-id]))

(a/reg-event-fx
  :storage/->files-selected-to-upload
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      ; A storage--file-selector input on-change eseménye indítja el a feltöltés inicializálását.
      (if-let [any-file-selected? (engine/any-file-selected?)]
              {:db (r store-uploading-props! db uploader-id)
               :dispatch-if [any-file-selected? [:storage/render-file-uploader! uploader-id]]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/abort-file-uploading!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ uploader-id]]
      {:db (r clean-file-uploader! db uploader-id)
       :dispatch [:ui/close-popup! (keyword/add-namespace :storage uploader-id)]}))

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
