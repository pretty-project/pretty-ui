
(ns server-extensions.products.product-editor
    (:require [x.server-core.api :as a]
              [server-plugins.item-editor.api :as item-editor]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-editor/initialize! :products :product]})
