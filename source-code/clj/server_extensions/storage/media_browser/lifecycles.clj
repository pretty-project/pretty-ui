
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.media-browser.lifecycles
    (:require [x.server-core.api :as a]
              [server-extensions.storage.engine :as engine]
              [server-plugins.item-browser.api  :as item-browser]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :storage :media
                                                {:root-item-id engine/ROOT-DIRECTORY-ID
                                                 :label-key    :alias
                                                 :path-key     :path
                                                 :search-keys  [:alias]}]})
