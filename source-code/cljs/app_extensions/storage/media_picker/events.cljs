
(ns app-extensions.storage.media-picker.events
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.engine :as engine]
              [app-plugins.item-browser.api  :as item-browser]))


;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- load-media-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id picker-props]]
  ;(as-> db % (r item-browser/set-current-item-id! % :storage :media engine/ROOT-DIRECTORY-ID)))
  db)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/load-media-picker!
  ; @param (keyword)
  (fn [{:keys [db]} event-vector]
      (let [picker-id    (a/event-vector->second-id   event-vector)
            picker-props (a/event-vector->first-props event-vector)]
           {:db (r load-media-picker! db picker-id picker-props)
            :dispatch [:storage/render-media-picker! picker-id picker-props]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/->media-item-picked
  (fn [{:keys [db]} [_ item-dex {:keys [id mime-type] :as item-props}]]
      (case mime-type "storage/directory" {:dispatch-n [[:item-browser/set-current-item-id! :storage :media id]
                                                        [:item-browser/load-browser! :storage :media]]}
                                          [:storage/->file-picked item-dex item-props])))
