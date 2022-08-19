

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.effects
    (:require [x.app-core.api                     :as a :refer [r]]
              [x.app-db.api                       :as db]
              [x.app-router.route-handler.helpers :as route-handler.helpers]
              [x.app-router.route-handler.events  :as route-handler.events]
              [x.app-router.route-handler.subs    :as route-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :router/change-to!
  ; @param (string) route-string
  ;
  ; @usage
  ;  [:router/change-to! "/my-route"]
  (fn [{:keys [db]} [_ route-string]]
      ; - A [:router/change-to! ...] esemény lecseréli az aktuális útvonalat, a hozzátartozó
      ;   események megtörténése nélkül.
      ;
      ; - Az útvonal használata előtt az útvonal-kezelőt {:change-mode? true} állapotba állítja,
      ;   ezért a kezelő figyelmen kívül hagyja az útvonalhoz hozzárendelt eseményeket.
      {:db       (r route-handler.events/set-change-mode! db)
       :dispatch [:router/go-to! route-string]}))

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
  ;
  ; @usage
  ;  [:router/go-to! "/@app-home/my-route/:my-param"]
  (fn [{:keys [db]} [_ route-string]]
      ; A [:router/go-to! ...] esemény a route-string paraméterként átadott útvonalban ...
      ; ... behelyettesíti a "/@app-home" kifejezést az aktuális értékével.
      ; ... behelyettesíti az útvonal-paramétereket az aktuális értékeikkel.
      ; ... megtartja az aktuális debug-mode értékét.
      (let [route-string (r route-handler.subs/use-app-home    db route-string)
            route-string (r route-handler.subs/use-path-params db route-string)
            route-string (r route-handler.subs/use-debug-mode  db route-string)]
           (if (r route-handler.subs/reload-same-path? db route-string)
               {:dispatch [:router/handle-route! route-string]}
               {:fx       [:router/navigate!     route-string]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :router/handle-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-id route-string]]
      (let [previous-route-id (r route-handler.subs/get-current-route-id db)]
           {:dispatch-n [(if-let [on-leave-event (get-in db [:router :route-handler/client-routes previous-route-id :on-leave-event])]
                                 (if (r route-handler.subs/route-id-changed? db route-id) on-leave-event))
                         (if-let [client-event (get-in db [:router :route-handler/client-routes route-id :client-event])]
                                 client-event)]})))

(a/reg-event-fx
  :router/handle-login!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-id route-string]]
      {:dispatch-n [[:boot-loader/set-restart-target! route-string]
                    [:views/render-login-box!]]}))

(a/reg-event-fx
  :router/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-string]]
      (let [route-id (r route-handler.subs/match-route-id db route-string)]
           (if-let [change-mode? (get-in db [:router :route-handler/meta-items :change-mode?])]
                   ; Ha az útvonal-kezelő {:change-mode? true} állapotban van ...
                   {:db (r route-handler.events/handle-route! db route-id route-string)}
                   ; Ha az útvonal-kezelő NINCS {:change-mode? true} állapotban ...
                   (if (r route-handler.subs/require-authentication? db route-id)
                       ; Ha az útvonal kezeléséhez bejelentkezés szükséges ...
                       {:db       (r route-handler.events/handle-route! db route-id route-string)
                        :dispatch [:router/handle-login! route-id route-string]}
                       ; Ha az útvonal kezeléséhez NEM szüksége bejelentkezés ...
                       {:db       (r route-handler.events/handle-route! db route-id route-string)
                        :dispatch [:router/handle-events! route-id route-string]})))))

(a/reg-event-fx
  :router/init-router!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r route-handler.events/init-router! db)
       :fx [:router/configure-navigation!]}))
