
(ns app-extensions.storage.media-picker.events
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-ui.api   :as ui]
              [app-extensions.storage.engine :as engine]
              [app-plugins.item-browser.api  :as item-browser]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/load!
  [a/debug!]
  ; @param (keyword) picker-id
  ; @param (map) (picker-props)
  (fn [{:keys [db]} event-vector]
      (let [picker-id    (a/event-vector->second-id   event-vector)
            picker-props (a/event-vector->first-props event-vector)]
           (if-not (r ui/element-rendered? db :popups :storage.media-picker/view)
                   [:storage.media-picker/render! picker-id picker-props]))))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/->item-picked
  [a/debug!]
  (fn [{:keys [db]} [_ item-dex {:keys [id mime-type] :as item-props}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          [:storage.media-picker/->file-picked item-dex item-props])))
