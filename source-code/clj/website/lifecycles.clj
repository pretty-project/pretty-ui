
(ns website.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:x.environment/add-css! {:uri "/css/website/components.css"}]
                                 [:x.environment/add-css! {:uri "/css/website/effects.css"}]]}})
