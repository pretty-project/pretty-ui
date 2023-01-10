
(ns pretty-css.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/add-css! {:uri "/pretty-css.min.css" :weight 1
                                             :dev-resources ["public/pretty-css"]}]})
