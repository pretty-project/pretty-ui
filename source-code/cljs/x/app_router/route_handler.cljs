
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v3.1.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler
    (:require [accountant.core   :as accountant]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.string :as string]
              [mid-fruits.uri    :as uri]
              [mid-fruits.vector :as vector]
              [reitit.frontend   :as reitit]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-user.api    :as user]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#3387 ----------------------------------------------------------------

; @name app-home
;  "/admin"
;
; @name abs-route-string
;  "/admin/clients"
;
; @name rel-route-string
;  "/clients"
;
; @name route-string
;  "/products/big-green-bong?type=hit#order"
;
; @name route-path
;  "/products/big-green-bong"
;
; @name route-template
;  "/products/:product-id"
;
; @name route-tail
;  "type=hit#order"
;
; @name route-query-string
;  "type=hit"
;
; @name route-query-params
;  {"type" "hit"}
;
; @name route-fragment
;  "order"
;
; @name client-event
;  [:products/render!]
;
; @name route-id
;  :products/route



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def RELOAD-SAME-PATH? true)

; @constant (map)
(def DEFAULT-ROUTES
     {:page-not-found {:client-event   [:views/render-error-page! :page-not-found]
                       :route-template "/page-not-found"}
      :reboot         {:client-event   [:boot-loader/reboot-app!]
                       :route-template "/reboot"}})

; @constant (map)
(def ACCOUNTANT-CONFIG
     {:nav-handler  #(a/dispatch   [:router/handle-route!      %])
      :path-exists? #(a/subscribed [:router/route-path-exists? %])
      :reload-same-path? false})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn valid-route-path
  ; @param (string) route-path
  ;
  ; @example
  ;  (router/valid-route-path "my-route")
  ;  =>
  ;  "/my-route"
  ;
  ; @example
  ;  (router/valid-route-path "/my-route")
  ;  =>
  ;  "/my-route"
  ;
  ; @example
  ;  (router/valid-route-path "/my-route/")
  ;  =>
  ;  "/my-route"
  ;
  ; @return (string)
  [route-path]   ; 1.
  (-> route-path (string/not-ends-with! "/")
                 ; 2.
                 (string/starts-with!   "/")))

(defn- routes->router-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (routes->router-routes {:route-id {:route-template "/"}})
  ;  =>
  ;  [["/" :route-id]]
  ;
  ; @return (vector)
  [routes]
  (reduce-kv #(if-let [route-template (:route-template %3)]
                      (vector/conj-item %1 [route-template %2]) %1)
              (param [])
              (param routes)))

(defn- route-string->rel-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ; @param (string) app-home
  ;
  ; @example
  ;  (relative-route-string "/my-route" "/admin")
  ;  =>
  ;  "/my-route"
  ;
  ; @example
  ;  (relative-route-string "/admin/my-route" "/admin")
  ;  =>
  ;  "/my-route"
  ;
  ; @example
  ;  (relative-route-string "/my-route" "/")
  ;  =>
  ;  "/my-route"
  ;
  ; @return (string)
  [route-string app-home]
  (let [rel-route-string (string/not-starts-with! route-string app-home)]
       (string/starts-with! rel-route-string "/")))

(defn- route-string->abs-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ; @param (string) app-home
  ;
  ; @example
  ;  (relative-route-string "/my-route" "/admin")
  ;  =>
  ;  "/admin/my-route"
  ;
  ; @example
  ;  (relative-route-string "/admin/my-route" "/admin")
  ;  =>
  ;  "/admin/my-route"
  ;
  ; @example
  ;  (relative-route-string "/my-route" "/")
  ;  =>
  ;  "/my-route"
  ;
  ; @return (string)
  [route-string app-home]
  (string/starts-with! route-string app-home))



;; -- Debug subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-debug-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (string)
  [db [_ route-string]]
  (if-let [debug-mode (r a/get-debug-mode db)]
          (uri/uri<-query-param route-string debug-mode)
          (param                route-string)))



;; -- Router subscriptions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-home
  ; @return (string)
  [db _]
  (let [app-home (r a/get-app-detail db :app-home)]
       (valid-route-path app-home)))

(defn app-home-specific?
  ; @return (boolean)
  [db _]
  (let [app-home (r get-app-home db)]
       (not= app-home "/")))

(defn get-routes
  ; @return (map)
  [db _]
  (get-in db (db/path ::client-routes)))

(defn- get-router-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vector)
  [db _]
  (let [routes (r get-routes db)]
       (routes->router-routes routes)))

(defn- get-router
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  [db _]
  (let [router-routes (r get-router-routes db)]
       (reitit/router router-routes)))

(defn- get-route-match
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-path
  ;
  ; @return (map)
  ;  https://github.com/metosin/reitit
  [db [_ route-path]]
  (let [router (r get-router db)]
       (reitit/match-by-path router route-path)))

(defn- route-path-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-path
  ;
  ; @return (boolean)
  [db [_ route-path]]
  (let [route-match (r get-route-match db route-path)]
       (boolean route-match)))

(a/reg-sub :router/route-path-exists? route-path-exists?)



;; -- Route subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-route-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ route-id prop-id]]
  (get-in db (db/path ::client-routes route-id prop-id)))

(defn- get-route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (string)
  [db [_ route-id]]
  (r get-route-prop db route-id :route-template))

(defn- get-client-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (metamorphic-event)
  [db [_ route-id]]
  (r get-route-prop db route-id :client-event))

(defn- get-on-leave-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (metamorphic-event)
  [db [_ route-id]]
  (r get-route-prop db route-id :on-leave-event))

(defn- route-restricted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [route-restricted? (r get-route-prop db route-id :restricted?)]
       (boolean route-restricted?)))

(defn- application-route?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (r route-restricted? db route-id))

(defn- website-route?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [application-route? (r application-route? db route-id)]
       (not application-route?)))

(defn- require-authentication?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (and (r route-restricted?       db route-id)
       (r user/user-unidentified? db)))



;; -- Match subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- match-route-id-by-rel-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) rel-route-string
  ;  A relatív útvonal
  ;  Pl.: "/my-route"
  ;
  ; @return (keyword)
  [db [_ rel-route-string]]
  (if (r route-path-exists? db rel-route-string)
      (get-in (r get-route-match db rel-route-string) [:data :name])
      (return :page-not-found)))

(defn- match-route-id-by-abs-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) abs-route-string
  ;  A címsorba írt útvonal
  ;  Pl.: "/admin/my-route"
  ;
  ; @return (keyword)
  [db [_ abs-route-string]]
  (if (r app-home-specific? db)

      ; If app-home is "/something" ...
      (let [app-home         (r get-app-home db)
            rel-route-string (route-string->rel-route-string abs-route-string app-home)]
           (if (r route-path-exists? db rel-route-string)
               ; If route-path exists ...
               (let [route-id (get-in (r get-route-match db rel-route-string) [:data :name])]
                          ; If route is application-route and it starts with app-home ...
                    (cond (and (r application-route?  db route-id)
                               (not= abs-route-string rel-route-string))
                          (return route-id)
                          ; If route is application-route but not starts with app-home ...
                          (and (r application-route?  db route-id)
                               (=    abs-route-string rel-route-string))
                          (return :page-not-found)
                          ; If route is website-route ...
                          (r website-route? db route-id)
                          (return route-id)))

               ; If route-path not exists ...
               (return :page-not-found)))

      ; If app-home is "/" ...
      (if (r route-path-exists? db abs-route-string)
          ; If route-path exists ...
          (get-in (r get-route-match db abs-route-string) [:data :name])
          ; If route-path not exists ...
          (return :page-not-found))))



;; -- Current route subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-route-string
  ; @usage
  ;  (r router/get-current-route-string db)
  ;
  ; @return (string)
  [db _]
  (get-in db (db/meta-item-path ::client-routes :route-string)))

(defn- get-current-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (r match-route-id-by-abs-route-string db current-route-string)))

(defn get-current-route-path
  ; @usage
  ;  (r router/get-current-route-path db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->path current-route-string)))

(defn get-current-route-template
  ; @usage
  ;  (r router/get-current-route-template db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db (db/path ::client-routes current-route-id :route-template))))

(defn get-current-route-path-params
  ; @usage
  ;  (r router/get-current-route-path-params db)
  ;
  ; @return (map)
  [db _]
  (let [current-route-string   (r get-current-route-string   db)
        current-route-template (r get-current-route-template db)
        app-home               (r get-app-home               db)
        rel-route-string       (route-string->rel-route-string current-route-string app-home)]
       (uri/uri->path-params rel-route-string current-route-template)))

; @usage
;  [:router/get-current-route-path-params]
(a/reg-sub :router/get-current-route-path-params get-current-route-path-params)

(defn get-current-route-path-param
  ; @param (keyword) param-id
  ;
  ; @usage
  ;  (r router/get-current-route-path-param db :my-param)
  ;
  ; @return (*)
  [db [_ param-id]]
  (let [current-route-path-params (r get-current-route-path-params db)]
       (get current-route-path-params param-id)))

; @usage
;  [:router/get-current-route-path-params :my-param]
(a/reg-sub :router/get-current-route-path-param get-current-route-path-param)

(defn current-route-path-param?
  ; @param (keyword) param-id
  ; @param (string) param-value
  ;
  ; @usage
  ;  (r router/current-route-path-param? db :my-param "my-value")
  ;
  ; @return (boolean)
  [db [_ param-id param-value]]
  (let [current-route-path-param (r get-current-route-path-param db param-id)]
       (= current-route-path-param param-value)))

; @usage
;  [:router/current-route-path-param? :my-param "my-value"]
(a/reg-sub :router/current-route-path-param? current-route-path-param?)

(defn get-current-route-query-params
  ; @usage
  ;  (r router/get-current-route-query-params db)
  ;
  ; @return (map)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->query-params current-route-string)))

(defn get-current-route-query-param
  ; @param (keyword) param-id
  ;
  ; @usage
  ;  (r router/get-current-route-query-param db :my-param)
  ;
  ; @return (*)
  [db [_ param-id]]
  (let [current-route-query-params (r get-current-route-query-params db)]
       (get current-route-query-params param-id)))

(defn get-current-route-fragment
  ; @usage
  ;  (r router/get-current-route-fragment db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->fragment current-route-string)))

(defn get-current-route-parent
  ; @usage
  ;  (r router/get-current-route-parent db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db (db/path ::client-routes current-route-id :route-parent))))



;; -- Handle subscriptions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn at-home?
  ; @usage
  ;  (r router/at-home? db)
  ;
  ; @return (boolean)
  [db _]
  (let [app-home           (r get-app-home           db)
        current-route-path (r get-current-route-path db)]
       (= current-route-path app-home)))

(a/reg-sub :router/at-home? at-home?)

(defn- route-id-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [current-route-id (r get-current-route-id db)]
       (not= route-id current-route-id)))

(defn- reload-same-path?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (boolean)
  [db [_ route-string]]
  (let [current-route-string (r get-current-route-string db)]
       (boolean (and (param RELOAD-SAME-PATH?)
                     (= route-string current-route-string)))))

(defn- get-history
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keywords in vector)
  [db _]
  (get-in db (db/meta-item-path ::client-routes :history)))

(defn- get-previous-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [history (r get-history db)]
       (vector/last-item history)))

(defn get-router-data
  ; @usage
  ;  (r router/get-router-data db)
  ;
  ; @return (map)
  [db _]
  {:route-string       (r get-current-route-string       db)
   :route-id           (r get-current-route-id           db)
   :route-path         (r get-current-route-path         db)
   :route-template     (r get-current-route-template     db)
   :route-path-params  (r get-current-route-path-params  db)
   :route-query-params (r get-current-route-query-params db)
   :route-fragment     (r get-current-route-fragment     db)})

; @usage
;  [:router/get-router-data]
(a/reg-sub :router/get-router-data get-router-data)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-current-route-string!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-string]]
  (assoc-in db (db/meta-item-path ::client-routes :route-string) route-string))

(defn- store-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-string]]
  (r store-current-route-string! db route-string))

(defn- set-default-routes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [stored-routes (r get-routes db)]
       ; A szerverről érkezett client-routes útvonalak magasabb prioritásúak,
       ; mint a DEFAULT-ROUTES útvonalak.
       ; Így lehetséges a szerver-oldalon beállított útvonalakkal felülírni
       ; a kliens-oldali DEFAULT-ROUTES útvonalakat.
       (assoc-in db (db/path ::client-routes) (merge DEFAULT-ROUTES stored-routes))))

(defn- reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (r db/apply! db (db/meta-item-path ::client-routes :history) vector/conj-item route-id))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :router/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-string]]
      (let [route-id          (r match-route-id-by-abs-route-string db route-string)
            previous-route-id (r get-current-route-id db)
            on-leave-event    (r get-on-leave-event   db previous-route-id)]
                           ; Store the current route
           {:db (as-> db % (r store-current-route! % route-string)
                           ; Make history
                           (r reg-to-history!      % route-id))
                         ; Dispatch on-leave-event if ...
            :dispatch-n [(if-let [on-leave-event (r get-on-leave-event db previous-route-id)]
                                 (if (r route-id-changed? db route-id) on-leave-event))
                         ; Render login screen if ...
                         (if (r require-authentication? db route-id)
                             [:views/render-login-box!])
                         ; Set restart-target if ...
                         (if (r require-authentication? db route-id)
                             [:boot-loader/set-restart-target! route-string])
                         ; Dispatch client-event if ...
                         (if-let [client-event (r get-client-event db route-id)]
                                 (if-not (r require-authentication? db route-id) client-event))]})))

(a/reg-event-fx
  :router/go-home!
  ; @usage
  ;  [:router/go-home!]
  (fn [{:keys [db]} _]
      (let [app-home (r get-app-home db)]
           [:router/go-to! app-home])))

(a/reg-event-fx
  :router/go-back!
  ; @usage
  ;  [:router/go-back!]
  [:router/navigate-back!])

(a/reg-event-fx
  :router/go-up!
  ; @usage
  ;  [:router/go-up!]
  (fn [{:keys [db]} _]
      (let [route-parent (r get-current-route-parent db)]
           [:router/go-to! route-parent])))

(a/reg-event-fx
  :router/go-to!
  ; @param (string) rel-route-string
  ;
  ; @usage
  ;  [:router/go-to! "/products/big-green-bong?type=hit#order"]
  (fn [{:keys [db]} [_ rel-route-string]]
      (let [app-home          (r get-app-home                       db)
            route-id          (r match-route-id-by-rel-route-string db rel-route-string)
            route-restricted? (r route-restricted?                  db route-id)
            abs-route-string  (if route-restricted? (route-string->abs-route-string rel-route-string app-home)
                                                    (param                          rel-route-string))
            ; Az applikáció az útvonalváltás után is debug módban marad
            abs-route-string (r get-debug-route-string db abs-route-string)]
           (if (r reload-same-path? db abs-route-string)
               [:router/handle-route! abs-route-string]
               [:router/navigate!     abs-route-string]))))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- configure-navigation!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (accountant/configure-navigation! ACCOUNTANT-CONFIG))

(a/reg-fx :router/configure-navigation! configure-navigation!)

(defn- navigate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  [route-string]
  (accountant/navigate! route-string))

(a/reg-handled-fx :router/navigate! navigate!)

(defn- navigate-back!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [history (.-history js/window)]
       (.back history)))

(a/reg-handled-fx :router/navigate-back! navigate-back!)

(defn- dispatch-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (accountant/dispatch-current!))

(a/reg-handled-fx :router/dispatch-current-route! dispatch-current-route!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :router/initialize!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
                      ; Set default routes
      {:db (as-> db % (r set-default-routes! % DEFAULT-ROUTES)
                      ; Store debug subscriber
                      (r db/set-item!        % (db/meta-item-path ::client-routes :debug) [:router/get-router-data]))
       ; Configure navigation
       :router/configure-navigation! nil}))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:router/initialize!]})
