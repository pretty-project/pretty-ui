
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.media-browser.lifecycles
    (:require [plugins.item-browser.api]
              [server-extensions.storage.engine :as engine]
              [x.server-core.api                :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :storage :media
                                                {:on-load [:storage.media-browser/load-browser!]
                                                 :route-template "/@app-home/storage/:item-id"}]})
