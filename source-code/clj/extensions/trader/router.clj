
(ns extensions.trader.router
    (:require [x.server-core.api :as a :refer [r]]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (metamorphic-event)
  [_ _]
  [:router/add-route! :trader/route
                      {:route-template "/@app-home/trader"
                       :client-event   [:trader/load-router!]
                       :restricted?    true
                       :on-leave-event [:trader/->route-leaved]}])

(defn- add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (metamorphic-event)
  [_ _]
  [:router/add-route! :trader/extended-route
                      {:route-template "/@app-home/trader/:route-id"
                       :client-event   [:trader/load-router!]
                       :restricted?    true
                       :on-leave-event [:trader/->route-leaved]}])

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/initialize!
  (fn [cofx _]
      {:dispatch-n [(r add-route!          cofx)
                    (r add-extended-route! cofx)]}))

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:trader/initialize!]})
