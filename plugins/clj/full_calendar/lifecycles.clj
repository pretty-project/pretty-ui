
(ns full-calendar.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/add-css! {:uri "/plugins/full-calendar.min.css" :js-build :app
                                             :dev-resources ["public/plugins/full-calendar"]}]})
