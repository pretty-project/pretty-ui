
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.media-browser.lifecycles
    (:require [server-plugins.item-browser.api]
              [server-extensions.storage.engine :as engine]
              [x.server-core.api                :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :storage :media
                                                {:base-route "/@app-home/storage"
                                                 :on-load    [:storage.media-browser/load-browser!]}]})
