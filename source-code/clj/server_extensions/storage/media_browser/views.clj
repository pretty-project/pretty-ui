
(ns server-extensions.storage.media-browser.views
    (:require [x.server-core.api :as a]
              [server-extensions.storage.engine :as engine]
              [server-plugins.item-browser.api  :as item-browser]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:item-browser/initialize! :storage :media
                                              {:label-key    :alias
                                               :path-key     :path
                                               :root-item-id engine/ROOT-DIRECTORY-ID
                                               :search-keys [:alias]}]})
                                               ;:routed? false}]})
