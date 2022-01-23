
(ns app-extensions.storage.media-browser.events
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [app-plugins.item-browser.api :as item-browser]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage)]
           (case selected-option :upload-files!
                                 [:storage/load-file-uploader!     {:destination-id destination-id}]
                                 :create-directory!
                                 [:storage/load-directory-creator! {:destination-id destination-id}]))))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/->media-item-clicked
  (fn [{:keys [db]} [_ item-dex {:keys [id mime-type] :as item-props}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          [:storage/->file-clicked item-dex item-props])))

(a/reg-event-fx
  :storage/->file-clicked
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
  :storage/load-media-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:storage/render-media-browser!])
