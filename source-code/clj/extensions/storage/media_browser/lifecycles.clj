
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
                                                              {:handler-key :storage.media-browser
                                                               :on-route [:storage.media-browser/load-browser!]
                                                               :route-template "/@app-home/storage/:item-id"
                                                               :route-title :storage}]]}})
