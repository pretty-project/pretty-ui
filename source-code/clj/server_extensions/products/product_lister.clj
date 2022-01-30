
(ns server-extensions.products.product-lister
    (:require [x.server-core.api :as a]
              [server-plugins.item-lister.api :as item-lister]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/initialize-lister! :products :product]})
