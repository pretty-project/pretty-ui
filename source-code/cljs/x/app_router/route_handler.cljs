
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v3.4.8
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler
    (:require [accountant.core     :as accountant]
              [mid-fruits.candy    :refer [param return]]
              [mid-fruits.uri      :as uri]
              [mid-fruits.vector   :as vector]
              [reitit.frontend     :as reitit]
              [x.app-core.api      :as a :refer [r]]
              [x.app-db.api        :as db]
              [x.app-router.engine :as engine]
              [x.app-user.api      :as user]
              [x.mid-router.route-handler :as route-handler]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#3387 ----------------------------------------------------------------

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
(def DEFAULT-ROUTES {:page-not-found {:client-event   [:views/render-error-page! :page-not-found]
                                      :route-template "/page-not-found"}
                     :reboot         {:client-event   [:boot-loader/reboot-app!]
                                      :route-template "/reboot"}})

; @constant (map)
(def ACCOUNTANT-CONFIG {:nav-handler  #(a/dispatch   [:router/handle-route!          %])
                        :path-exists? #(a/subscribed [:router/route-template-exists? %])
                        :reload-same-path? false})



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler
(def get-app-home route-handler/get-app-home)



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

(defn get-client-routes
  ; @return (map)
  [db _]
  (get-in db (db/path :router/client-routes)))

(defn- get-router-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vector)
  [db _]
  (let [client-routes (r get-client-routes db)]
       (engine/routes->router-routes client-routes)))

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
  ; @param (string) route-template
  ;
  ; @return (map)
  ;  https://github.com/metosin/reitit
  [db [_ route-template]]
  (let [router (r get-router db)]
       (reitit/match-by-path router route-template)))

(defn- route-template-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (boolean)
  [db [_ route-string]]
  (let [route-match (r get-route-match db route-string)]
       (boolean route-match)))

(a/reg-sub :router/route-template-exists? route-template-exists?)



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
  (get-in db (db/path :router/client-routes route-id prop-id)))

(defn- route-restricted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [route-restricted? (r get-route-prop db route-id :restricted?)]
       (boolean route-restricted?)))

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

(defn- match-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (keyword)
  [db [_ route-string]]
  (if (r route-template-exists? db route-string)
      (let [route-match (r get-route-match db route-string)]
           (get-in route-match [:data :name]))
      (return :page-not-found)))



;; -- Current route subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-route-string
  ; @usage
  ;  (r router/get-current-route-string db)
  ;
  ; @return (string)
  [db _]
  (get-in db (db/meta-item-path :router/client-routes :route-string)))

; @usage
;  [:router/get-current-route-string]
(a/reg-sub :router/get-current-route-string get-current-route-string)

(defn get-current-route-id
  ; @usage
  ;  (r router/get-current-route-id db)
  ;
  ; @return (keyword)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (r match-route-id db current-route-string)))

; @usage
;  [:router/get-current-route-id]
(a/reg-sub :router/get-current-route-id get-current-route-id)

(defn get-current-route-path
  ; @usage
  ;  (r router/get-current-route-path db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->path current-route-string)))

; @usage
;  [:router/get-current-route-path]
(a/reg-sub :router/get-current-route-path get-current-route-path)

(defn get-current-route-template
  ; @usage
  ;  (r router/get-current-route-template db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db (db/path :router/client-routes current-route-id :route-template))))

; @usage
;  [:router/get-current-route-template]
(a/reg-sub :router/get-current-route-template get-current-route-template)

(defn get-current-route-path-params
  ; @usage
  ;  (r router/get-current-route-path-params db)
  ;
  ; @return (map)
  [db _]
  (let [current-route-string   (r get-current-route-string   db)
        current-route-template (r get-current-route-template db)]
       (uri/uri->path-params current-route-string current-route-template)))

; @usage
;  [:router/get-current-route-path-params]
(a/reg-sub :router/get-current-route-path-params get-current-route-path-params)

(defn get-current-route-path-param
  ; @param (keyword) param-key
  ;
  ; @usage
  ;  (r router/get-current-route-path-param db :my-param)
  ;
  ; @return (string)
  [db [_ param-key]]
  (let [current-route-path-params (r get-current-route-path-params db)]
       (get current-route-path-params param-key)))

; @usage
;  [:router/get-current-route-path-param :my-param]
(a/reg-sub :router/get-current-route-path-param get-current-route-path-param)

(defn current-route-path-param?
  ; @param (keyword) param-key
  ; @param (string) param-value
  ;
  ; @usage
  ;  (r router/current-route-path-param? db :my-param "My value")
  ;
  ; @return (boolean)
  [db [_ param-key param-value]]
  (let [current-route-path-param (r get-current-route-path-param db param-key)]
       (= current-route-path-param param-value)))

; @usage
;  [:router/current-route-path-param? :my-param "My value"]
(a/reg-sub :router/current-route-path-param? current-route-path-param?)

(defn get-current-route-query-params
  ; @usage
  ;  (r router/get-current-route-query-params db)
  ;
  ; @return (map)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->query-params current-route-string)))

; @usage
;  [:router/get-current-route-query-param]
(a/reg-sub :router/get-current-route-query-params get-current-route-query-params)

(defn get-current-route-query-param
  ; @param (keyword) param-key
  ;
  ; @usage
  ;  (r router/get-current-route-query-param db :my-param)
  ;
  ; @return (string)
  [db [_ param-key]]
  (let [current-route-query-params (r get-current-route-query-params db)]
       (get current-route-query-params param-key)))

; @usage
;  [:router/get-current-route-query-param :my-param]
(a/reg-sub :router/get-current-route-query-param get-current-route-query-param)

(defn current-route-query-param?
  ; @param (keyword) param-key
  ; @param (string) param-value
  ;
  ; @usage
  ;  (r router/current-route-query-param? db :my-param "My value")
  ;
  ; @return (boolean)
  [db [_ param-key param-value]]
  (let [current-route-query-param (r get-current-route-query-param db param-key)]
       (= current-route-query-param param-value)))

; @usage
;  [:router/current-route-query-param? :my-param "My value"]
(a/reg-sub :router/current-route-query-param? current-route-query-param?)

(defn get-current-route-fragment
  ; @usage
  ;  (r router/get-current-route-fragment db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->fragment current-route-string)))

; @usage
;  [:router/get-current-route-fragment]
(a/reg-sub :router/get-current-route-fragment get-current-route-fragment)

(defn get-current-route-parent
  ; @usage
  ;  (r router/get-current-route-parent db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db (db/path :router/client-routes current-route-id :route-parent))))

; @usage
;  [:router/get-current-route-parent]
(a/reg-sub :router/get-current-route-parent get-current-route-parent)



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

; @usage
;  [:router/at-home?]
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
  (get-in db (db/meta-item-path :router/client-routes :history)))

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
  (assoc-in db (db/meta-item-path :router/client-routes :route-string) route-string))

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
  (let [client-routes (r get-client-routes db)]
       ; A szerverről érkezett client-routes útvonalak magasabb prioritásúak,
       ; mint a DEFAULT-ROUTES útvonalak.
       ; Így lehetséges a szerver-oldalon beállított útvonalakkal felülírni
       ; a kliens-oldali DEFAULT-ROUTES útvonalakat.
       (assoc-in db (db/path :router/client-routes) (merge DEFAULT-ROUTES client-routes))))

(defn- reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (r db/apply! db (db/meta-item-path :router/client-routes :history) vector/conj-item route-id))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :router/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-string]]
      (let [route-id          (r match-route-id db route-string)
            previous-route-id (r get-current-route-id db)]
                           ; Store the current route
           {:db (as-> db % (r store-current-route! % route-string)
                           ; Make history
                           (r reg-to-history!      % route-id))
                         ; Dispatch on-leave-event if ...
            :dispatch-n [(if-let [on-leave-event (r get-route-prop db previous-route-id :on-leave-event)]
                                 (if (r route-id-changed? db route-id) on-leave-event))
                         ; Render login screen if ...
                         (if (r require-authentication? db route-id)
                             [:views/render-login-box!])
                         ; Set restart-target if ...
                         (if (r require-authentication? db route-id)
                             [:boot-loader/set-restart-target! route-string])
                         ; Dispatch client-event if ...
                         (if-let [client-event (r get-route-prop db route-id :client-event)]
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
      (if (engine/variable-route-string? route-string)

          ; If route-string is variable ...
          (let [app-home     (r get-app-home db)
                route-string (engine/resolve-variable-route-string route-string app-home)
                ; Az applikáció az útvonalváltás után is debug módban marad
                route-string (r get-debug-route-string db route-string)]
               (if (r reload-same-path? db route-string)
                   [:router/handle-route! route-string]
                   [:router/navigate!     route-string]))

          ; If route-string is static ...
          (let [; Az applikáció az útvonalváltás után is debug módban marad
                route-string (r get-debug-route-string db route-string)]
               (if (r reload-same-path? db route-string)
                   [:router/handle-route! route-string]
                   [:router/navigate!     route-string])))))



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
  ; @param (string) abs-route-string
  [abs-route-string]
  (accountant/navigate! abs-route-string))

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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]; Set default routes
      {:db (as-> db % (r set-default-routes! % DEFAULT-ROUTES)
                      ; Store debug subscriber
                      (r db/set-item! % (db/meta-item-path :router/client-routes :debug) [:router/get-router-data]))
       ; Configure navigation
       :router/configure-navigation! nil}))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:router/initialize!]})
