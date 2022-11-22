
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.effects
    (:require [re-frame.api                   :as r :refer [r]]
              [x.router.route-handler.helpers :as route-handler.helpers]
              [x.router.route-handler.events  :as route-handler.events]
              [x.router.route-handler.subs    :as route-handler.subs]
              [x.views.api                    :as x.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.router/init-router!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r route-handler.events/init-router! db)
       :fx [:accountant/configure-navigation! {:nav-handler  route-handler.helpers/nav-handler-f
                                               :path-exists? route-handler.helpers/path-exists-f
                                               :reload-same-path? true}]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.router/dispatch-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:fx [:accountant/dispatch-current!]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.router/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-string]]
      (let [route-id (r route-handler.subs/match-route-id db route-string)]
           (if (r route-handler.subs/require-authentication? db route-id)
               ; Ha az útvonal kezeléséhez bejelentkezés szükséges ...
               {:db       (r route-handler.events/handle-route! db route-id route-string)
                :dispatch [:x.router/handle-login! route-id route-string]}
               ; Ha az útvonal kezeléséhez NEM szüksége bejelentkezés ...
               {:db       (r route-handler.events/handle-route! db route-id route-string)
                :dispatch [:x.router/handle-events! route-id route-string]}))))

(r/reg-event-fx :x.router/handle-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-id route-string]]
      ; XXX#0781
      (if-not (r route-handler.subs/swap-mode? db)
              (let [previous-route-id (r route-handler.subs/get-current-route-id db)]
                   {:dispatch-n [(if-let [on-leave-event (get-in db [:x.router :route-handler/client-routes previous-route-id :on-leave-event])]
                                         (if (r route-handler.subs/route-id-changed? db route-id) on-leave-event))
                                 (if-let [client-event (get-in db [:x.router :route-handler/client-routes route-id :client-event])]
                                         client-event)]})
              {:db (r route-handler.events/quit-swap-mode! db)})))

(r/reg-event-fx :x.router/handle-login!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-id route-string]]
      (let [login-screen (r x.views/get-login-screen db)]
           {:dispatch-n [[:x.boot-loader/set-restart-target! route-string]
                         login-screen]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.router/go-back!
  ; @usage
  ;  [:x.router/go-back!]
  {:fx [:x.environment/go-back!]})

(r/reg-event-fx :x.router/go-home!
  ; @usage
  ;  [:x.router/go-home!]
  (fn [{:keys [db]} _]
      (let [app-home (r route-handler.subs/get-app-home db)]
           [:x.router/go-to! app-home])))

(r/reg-event-fx :x.router/go-up!
  ; @usage
  ;  [:x.router/go-up!]
  (fn [{:keys [db]} _]
      (let [parent-route (r route-handler.subs/get-parent-route db)]
           [:x.router/go-to! parent-route])))

(r/reg-event-fx :x.router/swap-to!
  ; @param (string) route-string
  ; @param (map)(opt) router-props
  ;  {:parent-route (string)(opt)}
  ;
  ; @usage
  ;  [:x.router/swap-to! "/my-route"]
  ;
  ; @usage
  ;  [:x.router/swap-to! "/my-route" {...}]
  (fn [{:keys [db]} [_ route-string route-props]]
      ; XXX#0781
      ; A [:x.router/swap-to! ...] esemény lecseréli az aktuális útvonalat,
      ; a hozzárendelt események megtörténése nélkül.
      {:db       (r route-handler.events/set-swap-mode! db)
       :dispatch [:x.router/go-to! route-string route-props]}))

(r/reg-event-fx :x.router/go-to!
  ; @param (string) route-string
  ; @param (map)(opt) route-props
  ;  {:parent-route (string)(opt)}
  ;
  ; @usage
  ;  [:x.router/go-to! "/my-route"]
  ;
  ; @usage
  ;  [:x.router/go-to! "/my-route" {...}]
  ;
  ; @usage
  ;  [:x.router/go-to! "/my-app/your-route"]
  ;
  ; @usage
  ;  [:x.router/go-to! "/@app-home/your-route"]
  ;
  ; @usage
  ;  [:x.router/go-to! "/@app-home/my-route/:my-param"]
  (fn [{:keys [db]} [_ route-string route-props]]
      ; A [:x.router/go-to! ...] esemény a route-string paraméterként átadott útvonalban ...
      ; ... behelyettesíti a "/@app-home" kifejezést az aktuális értékével.
      ; ... behelyettesíti az útvonal-paramétereket az aktuális értékeikkel.
      ; ... megtartja az aktuális debug-mode értékét.
      (let [route-string (r route-handler.subs/use-app-home    db route-string)
            route-string (r route-handler.subs/use-path-params db route-string)
            route-string (r route-handler.subs/use-debug-mode  db route-string)]
           {:db (r route-handler.events/go-to! db route-string route-props)
            :fx [:accountant/navigate! route-string]})))
