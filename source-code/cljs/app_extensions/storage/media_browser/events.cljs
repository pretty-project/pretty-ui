
(ns app-extensions.storage.media-browser.events
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-ui.api     :as ui]
              [app-plugins.item-browser.api :as item-browser]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-creator-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) creator-id
  ; @param (map) creator-props
  ;
  ; @return (map)
  [db [_ _ creator-props]]
  (assoc-in db [:storage :directory-creator/meta-items] creator-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage)]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    {:destination-id destination-id}]
                                 :create-directory! [:storage.directory-creator/load-creator! {:destination-id destination-id}]))))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-lister/->item-clicked
  (fn [{:keys [db]} [_ item-dex {:keys [id mime-type] :as item-props}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          [:storage.media-browser/->file-clicked item-dex item-props])))

(a/reg-event-fx
  :storage.media-browser/->file-clicked
  (fn [_ _]
      [:ui/add-popup! :xxxx
                      {:body [:div [:div "Fájl letöltése"]
                                   ; Ha több van kijelölve akkor:
                                   ; És mivel nincs tömörítönk ezért most még disabled lesz
                                   [:div "Kijelölt fájlok letöltése"]]
                       :min-width :xs}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r ui/element-rendered? db :surface :storage.media-browser/view)
              [:storage.media-browser/render-browser!])))
      ;[:storage.media-picker/load-browser!]))
