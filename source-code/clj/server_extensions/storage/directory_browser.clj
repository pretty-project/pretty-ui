
(ns server-extensions.storage.directory-browser
    (:require [x.server-core.api :as a]
              [server-plugins.item-browser.api :as item-browser]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-browser/initialize! :storage :directory {}]})
