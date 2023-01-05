
(ns profiles.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/add-css! {:uri "/css/profiles/default.css" :compress? true :weight 0}]})
