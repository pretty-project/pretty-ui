
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.effects
    (:require [x.app-core.api                     :as a :refer [r]]
              [x.app-db.api                       :as db]
              [x.app-router.route-handler.config  :as route-handler.config]
              [x.app-router.route-handler.helpers :as route-handler.helpers]
              [x.app-router.route-handler.events  :as route-handler.events]
              [x.app-router.route-handler.subs    :as route-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :router/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-string]]
      (let [route-id          (r route-handler.subs/match-route-id       db route-string)
            previous-route-id (r route-handler.subs/get-current-route-id db)]
           {:db (as-> db % ; Store the current route
                           (r route-handler.events/store-current-route! % route-string)
                           ; Make history
                           (r route-handler.events/reg-to-history!      % route-id))
            :dispatch-n [; Dispatch on-leave-event if ...
                         (if-let [on-leave-event (get-in db [:router :route-handler/client-routes previous-route-id :on-leave-event])]
                                 (if (r route-handler.subs/route-id-changed? db route-id) on-leave-event))
                         ; Render login screen if ...
                         (if (r route-handler.subs/require-authentication? db route-id)
                             [:views/render-login-box!])
                         ; Set restart-target if ...
                         (if (r route-handler.subs/require-authentication? db route-id)
                             [:boot-loader/set-restart-target! route-string])
                         ; Dispatch client-event if ...
                         (if-let [client-event (get-in db [:router :route-handler/client-routes route-id :client-event])]
                                 (if-not (r route-handler.subs/require-authentication? db route-id) client-event))]})))

(a/reg-event-fx
  :router/go-home!
  ; @usage
  ;  [:router/go-home!]
  (fn [{:keys [db]} _]
      (let [app-home (r route-handler.subs/get-app-home db)]
           [:router/go-to! app-home])))

(a/reg-event-fx
  :router/go-back!
  ; @usage
  ;  [:router/go-back!]
  {:fx [:router/navigate-back!]})

(a/reg-event-fx
  :router/go-up!
  ; @usage
  ;  [:router/go-up!]
  (fn [{:keys [db]} _]
      (let [parent-route (r route-handler.subs/get-current-route-parent db)]
           [:router/go-to! parent-route])))

(a/reg-event-fx
  :router/go-to!
  ; @param (string) route-string
  ;
  ; @usage
  ;  [:router/go-to! "/my-route"]
  ;
  ; @usage
  ;  [:router/go-to! "/my-app/your-route"]
  ;
  ; @usage
  ;  [:router/go-to! "/@app-home/your-route"]
  (fn [{:keys [db]} [_ route-string]]
      (if (route-handler.helpers/variable-route-string? route-string)

          ; If route-string is variable ...
          (let [app-home     (r route-handler.subs/get-app-home db)
                route-string (route-handler.helpers/resolve-variable-route-string route-string app-home)
                ; Az applikáció az útvonalváltás után is debug módban marad
                route-string (r route-handler.subs/get-debug-route-string db route-string)]
               (if (r route-handler.subs/reload-same-path? db route-string)
                   {:dispatch [:router/handle-route! route-string]}
                   {:fx       [:router/navigate!     route-string]}))

          ; If route-string is static ...
          (let [; Az applikáció az útvonalváltás után is debug módban marad
                route-string (r route-handler.subs/get-debug-route-string db route-string)]
               (if (r route-handler.subs/reload-same-path? db route-string)
                   {:dispatch [:router/handle-route! route-string]}
                   {:fx       [:router/navigate!     route-string]})))))

(a/reg-event-fx
  :router/init-router!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {; Set default routes
       :db (r route-handler.events/set-default-routes! db route-handler.config/DEFAULT-ROUTES)
       ; Configure navigation
       :fx [:router/configure-navigation!]}))
