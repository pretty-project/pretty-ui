
(ns carousel.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/add-css! {:uri "/plugins/carousel.min.css" :js-build :site
                                             :dev-resources ["public/plugins/carousel"]}]})
