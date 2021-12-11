
(ns server-extensions.products.product-lister
    (:require [x.server-core.api :as a]
              [server-plugins.item-lister.api :as item-lister]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-lister/initialize! :products :product]})
