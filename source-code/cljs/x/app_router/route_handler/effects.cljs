
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.effects
    (:require [re-frame.api                       :as r :refer [r]]
              [x.app-router.route-handler.helpers :as route-handler.helpers]
              [x.app-router.route-handler.events  :as route-handler.events]
              [x.app-router.route-handler.subs    :as route-handler.subs]
              [x.views.api                        :as x.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :router/init-router!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r route-handler.events/init-router! db)
       :fx [:accountant/configure-navigation! {:nav-handler  route-handler.helpers/nav-handler-f
                                               :path-exists? route-handler.helpers/path-exists-f
                                               :reload-same-path? true}]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :router/dispatch-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:fx [:accountant/dispatch-current!]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :router/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-string]]
      (let [route-id (r route-handler.subs/match-route-id db route-string)]
           (if (r route-handler.subs/require-authentication? db route-id)
               ; Ha az útvonal kezeléséhez bejelentkezés szükséges ...
               {:db       (r route-handler.events/handle-route! db route-id route-string)
                :dispatch [:router/handle-login! route-id route-string]}
               ; Ha az útvonal kezeléséhez NEM szüksége bejelentkezés ...
               {:db       (r route-handler.events/handle-route! db route-id route-string)
                :dispatch [:router/handle-events! route-id route-string]}))))

(r/reg-event-fx :router/handle-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-id route-string]]
      ; XXX#0781
      (if-not (r route-handler.subs/swap-mode? db)
              (let [previous-route-id (r route-handler.subs/get-current-route-id db)]
                   {:dispatch-n [(if-let [on-leave-event (get-in db [:router :route-handler/client-routes previous-route-id :on-leave-event])]
                                         (if (r route-handler.subs/route-id-changed? db route-id) on-leave-event))
                                 (if-let [client-event (get-in db [:router :route-handler/client-routes route-id :client-event])]
                                         client-event)]})
              {:db (r route-handler.events/quit-swap-mode! db)})))

(r/reg-event-fx :router/handle-login!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-id route-string]]
      (let [login-screen (r x.views/get-login-screen db)]
           {:dispatch-n [[:boot-loader/set-restart-target! route-string]
                         login-screen]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :router/go-back!
  ; @usage
  ;  [:router/go-back!]
  {:fx [:environment/go-back!]})

(r/reg-event-fx :router/go-home!
  ; @usage
  ;  [:router/go-home!]
  (fn [{:keys [db]} _]
      (let [app-home (r route-handler.subs/get-app-home db)]
           [:router/go-to! app-home])))

(r/reg-event-fx :router/go-up!
  ; @usage
  ;  [:router/go-up!]
  (fn [{:keys [db]} _]
      (let [parent-route (r route-handler.subs/get-current-route-parent db)]
           [:router/go-to! parent-route])))

(r/reg-event-fx :router/swap-to!
  ; @param (string) route-string
  ; @param (map)(opt) router-props
  ;  {:route-parent (string)(opt)}
  ;
  ; @usage
  ;  [:router/swap-to! "/my-route"]
  ;
  ; @usage
  ;  [:router/swap-to! "/my-route" {...}]
  (fn [{:keys [db]} [_ route-string route-props]]
      ; XXX#0781
      ; A [:router/swap-to! ...] esemény lecseréli az aktuális útvonalat,
      ; a hozzárendelt események megtörténése nélkül.
      {:db       (r route-handler.events/set-swap-mode! db)
       :dispatch [:router/go-to! route-string route-props]}))

(r/reg-event-fx :router/go-to!
  ; @param (string) route-string
  ; @param (map)(opt) route-props
  ;  {:route-parent (string)(opt)}
  ;
  ; @usage
  ;  [:router/go-to! "/my-route"]
  ;
  ; @usage
  ;  [:router/go-to! "/my-route" {...}]
  ;
  ; @usage
  ;  [:router/go-to! "/my-app/your-route"]
  ;
  ; @usage
  ;  [:router/go-to! "/@app-home/your-route"]
  ;
  ; @usage
  ;  [:router/go-to! "/@app-home/my-route/:my-param"]
  (fn [{:keys [db]} [_ route-string route-props]]
      ; A [:router/go-to! ...] esemény a route-string paraméterként átadott útvonalban ...
      ; ... behelyettesíti a "/@app-home" kifejezést az aktuális értékével.
      ; ... behelyettesíti az útvonal-paramétereket az aktuális értékeikkel.
      ; ... megtartja az aktuális debug-mode értékét.
      (let [route-string (r route-handler.subs/use-app-home    db route-string)
            route-string (r route-handler.subs/use-path-params db route-string)
            route-string (r route-handler.subs/use-debug-mode  db route-string)]
           {:db (r route-handler.events/go-to! db route-string route-props)
            :fx [:accountant/navigate! route-string]})))
