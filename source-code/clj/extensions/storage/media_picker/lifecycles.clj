
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.lifecycles
    (:require [plugins.item-browser.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :storage.media-picker
                                                {:collection-name "storage"
                                                 :handler-key     :storage.media-browser
                                                 :item-namespace  :media}]})
