
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



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/load-media-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n [[:storage/render-media-browser!]]}))
