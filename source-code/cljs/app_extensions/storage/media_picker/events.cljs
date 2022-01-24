
(ns app-extensions.storage.media-picker.events
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.engine :as engine]
              [app-plugins.item-browser.api  :as item-browser]))


;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/load-media-picker!
  [a/debug!]
  ; @param (keyword)
  (fn [_ event-vector]
      (let [picker-id    (a/event-vector->second-id   event-vector)
            picker-props (a/event-vector->first-props event-vector)]
           {:dispatch [:storage/render-media-picker! picker-id picker-props]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/->media-item-picked
  [a/debug!]
  (fn [{:keys [db]} [_ item-dex {:keys [id mime-type] :as item-props}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          [:storage/->file-picked item-dex item-props])))
