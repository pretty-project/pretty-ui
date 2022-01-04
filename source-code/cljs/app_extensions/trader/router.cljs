
(ns app-extensions.trader.router
    (:require [mid-fruits.random :as random]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/init-app!
  {:dispatch-n [[:trader/connect-to-listener!]
                [:trader/connect-to-log!]
                [:trader/connect-to-account!]
                [:trader/init-monitor!]]})

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/load-router!
  (fn [{:keys [db]}]
      {:dispatch-n [[:trader/init-app!]
                    (if-let [route-id (r router/get-current-route-path-param db :route-id)]
                            (case route-id "edit-source-code" [:trader/load-editor!])
                            [:trader/load-main!])]}))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->route-leaved
  (fn [_ _]))
