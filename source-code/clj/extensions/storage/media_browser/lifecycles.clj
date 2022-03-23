
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-browser.lifecycles
    (:require [plugins.item-browser.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:environment/add-css! {:uri "/css/extensions/storage.media-browser.css"}]
                                 [:item-browser/init-browser! :storage :media
                                                              {:collection-name :storage
                                                               :handler-key     :storage.media-browser
                                                               :route-title     :storage
                                                               :on-route [:storage.media-browser/load-browser!]
                                                               :route-template "/@app-home/storage/:item-id"}]]}})
