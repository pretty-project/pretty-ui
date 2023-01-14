
(ns components.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/add-css! {:uri "/css/components.min.css"
                                             :dev-resources ["public/css/components"]}]})