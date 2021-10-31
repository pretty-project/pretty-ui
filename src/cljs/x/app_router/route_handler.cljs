
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.1.2
; Compatibility: x4.2.8



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
; @name route-event
;  [:products.views/render!]
;
; @name route-id
;  :products
;
; @name freezed-route
;  Az [:x.app-router/freeze-current-route!] esemény meghívásával lehetséges
;  az aktuális route-ot és annak adatait eltárolni és az esemény következő
;  meghívásáig az (r router/get-freeze-route-* db) subscription függvényekkel
;  az eltárolt route és annak adatai elérhetőek maradnak a Re-Frame adatbázisban.
;
;  Ha nincs az [:x.app-router/freeze-current-route!] esemény meghívásával eltárolt
;  route, akkor az (r router/get-freeze-route-* db) subscription függvények
;  az aktuális route-tal és annak adataival térnek vissza.
;
;  Bizonyos esetekben, ha egy UI felületen az aktuális route egyik pl. route-path-param
;  értékére van a tartalom feliratkozva, akkor a route változásakor a route-path-param
;  értéke már azelőtt megváltozik, hogy az új route-hoz tartozó {:route-event ...}
;  esemény megtörténne és ez a UI felületen hibás tartalom felvillanását okozhatja.



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (a/reg-lifecycles {:on-app-boot {:dispatch-n [[:x.app-router/add-route! {...}]
;                                                [:x.app-router/add-route! :my-route {...}]
;                                                [:x.app-router/add-route! :page-not-found {...}]
;                                                [:x.app-router/set-home! {...}]]}})



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def RELOAD-SAME-PATH? true)

; @constant (ms)
(def SCROLL-TO-FRAGMENT-DELAY 2000)

; @constant (px)
(def SCROLL-TO-FRAGMENT-OFFSET -50)

; @constant (map)
(def DEFAULT-ROUTES
     {:reboot {:route-event    [:x.boot-loader/reboot-app!]
               :route-template "/reboot"}
      :home   {:route-template "/"}})

; @constant (map)
(def ACCOUNTANT-CONFIG
     {:nav-handler  #(a/dispatch   [::handle-route!      %])
      :path-exists? #(a/subscribed [::route-path-exists? %])
      :reload-same-path? false})



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- fragment-id->dom-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) fragment-id
  ;
  ; @return (hiccup)
  [fragment-id]
  (str "x-fragment-marker--" (name fragment-id)))

(defn- route-id->at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-id
  ;
  ; @return (boolean)
  [route-id]
  (= route-id :home))

(defn- routes->router-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (routes->router-routes {:route-id {:route-template "/"}})
  ;  => [["/" :route-id]]
  ;
  ; @return (vector)
  [routes]
  (reduce-kv #(if-let [route-template (:route-template %3)]
                      (vector/conj-item %1 [route-template %2]) %1)
              (param [])
              (param routes)))



;; -- Subscriptions -----------------------------------------------------------
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

(defn- get-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::routes)))

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

(a/reg-sub ::route-path-exists? route-path-exists?)

(defn- get-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-path
  ;
  ; @return (keyword)
  [db [_ route-path]]
  (if (r route-path-exists? db route-path)
      (get-in (r get-route-match db route-path) [:data :name])
      (return :page-not-found)))

(defn- get-route-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ route-id prop-id]]
  (get-in db (db/path ::routes route-id prop-id)))

(defn- get-route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (string)
  [db [_ route-id]]
  (r get-route-prop db route-id :route-template))

(defn- get-route-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (metamorphic-event)
  [db [_ route-id]]
  (r get-route-prop db route-id :route-event))

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

(defn- require-authentication?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (and (r route-restricted?       db route-id)
       (r user/user-unidentified? db)))

(defn get-current-route-string
  ; @usage
  ;  (r router/get-current-route-string db)
  ;
  ; @return (string)
  [db _]
  (get-in db (db/meta-item-path ::routes :route-string)))

(defn- get-current-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (r get-route-id db current-route-string)))

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
       (get-in db (db/path ::routes current-route-id :route-template))))

(defn get-current-route-path-params
  ; @usage
  ;  (r router/get-current-route-path-params db)
  ;
  ; @return (map)
  [db _]
  (let [current-route-string   (r get-current-route-string   db)
        current-route-template (r get-current-route-template db)]
       (uri/uri->path-params current-route-string current-route-template)))

(a/reg-sub :x.app-router/get-current-route-path-params get-current-route-path-params)

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

(a/reg-sub :x.app-router/get-current-route-path-param get-current-route-path-param)

(defn current-route-path-param?
  ; @param (keyword) param-id
  ;
  ; @usage
  ;  (r router/current-route-path-param? db :my-param "my-value")
  ;
  ; @return (boolean)
  [db [_ param-id param-value]]
  (let [current-route-path-param (r get-current-route-path-param db param-id)]
       (= current-route-path-param param-value)))

(a/reg-sub :x.app-router/current-route-path-param? current-route-path-param?)

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

(defn get-freezed-route-string
  ; @usage
  ;  (r router/get-freezed-route-string db)
  ;
  ; @return (string)
  [db _]
  (get-in db (db/meta-item-path ::routes :freezed-route-string)
             (get-in db (db/meta-item-path ::routes :route-string))))

(defn- get-freezed-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [freezed-route-string (r get-freezed-route-string db)]
       (r get-route-id db freezed-route-string)))

(defn get-freezed-route-path
  ; @usage
  ;  (r router/get-freezed-route-path db)
  ;
  ; @return (string)
  [db _]
  (let [freezed-route-string (r get-freezed-route-string db)]
       (uri/uri->path freezed-route-string)))

(defn get-freezed-route-template
  ; @usage
  ;  (r router/get-freezed-route-template db)
  ;
  ; @return (string)
  [db _]
  (let [freezed-route-id (r get-freezed-route-id db)]
       (get-in db (db/path ::routes freezed-route-id :route-template))))

(defn get-freezed-route-path-params
  ; @usage
  ;  (r router/get-freezed-route-path-params db)
  ;
  ; @return (map)
  [db _]
  (let [freezed-route-string   (r get-freezed-route-string   db)
        freezed-route-template (r get-freezed-route-template db)]
       (uri/uri->path-params freezed-route-string freezed-route-template)))

(defn get-freezed-route-path-param
  ; @param (keyword) param-id
  ;
  ; @usage
  ;  (r router/get-freezed-route-path-param db :my-param)
  ;
  ; @return (*)
  [db [_ param-id]]
  (let [freezed-route-path-params (r get-freezed-route-path-params db)]
       (get freezed-route-path-params param-id)))

(defn get-freezed-route-query-params
  ; @usage
  ;  (r router/get-freezed-route-query-params db)
  ;
  ; @return (map)
  [db _]
  (let [freezed-route-string (r get-freezed-route-string db)]
       (uri/uri->query-params freezed-route-string)))

(defn get-freezed-route-query-param
  ; @param (keyword) param-id
  ;
  ; @usage
  ;  (r router/get-freezed-route-query-param db :my-param)
  ;
  ; @return (*)
  [db [_ param-id]]
  (let [freezed-route-query-params (r get-freezed-route-query-params db)]
       (get freezed-route-query-params param-id)))

(defn get-freezed-route-fragment
  ; @usage
  ;  (r router/get-freezed-route-fragment db)
  ;
  ; @return (string)
  [db _]
  (let [freezed-route-string (r get-freezed-route-string db)]
       (uri/uri->fragment freezed-route-string)))

(defn at-home?
  ; @usage
  ;  (r router/at-home? db)
  ;
  ; @return (boolean)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (route-id->at-home? current-route-id)))

(a/reg-sub :x.app-router/at-home? at-home?)

(defn- route-id-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [current-route-id (r get-current-route-id db)]
       (not= route-id current-route-id)))

(defn- scroll-to-fragment?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (and (not     (r require-authentication? db route-id))
       (boolean (r get-route-prop db route-id :scroll-to-fragment?))
       (some?   (get-in db (db/meta-item-path ::routes :route-fragment)))))

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
  (get-in db (db/meta-item-path ::routes :history)))

(defn- get-previous-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [history (r get-history db)]
       (vector/last-item history)))

(defn get-reserved-app-routes
  ; @return (strings in vector)
  [db _]
  (let [routes (r get-routes db)]
       (reduce-kv (fn [reserved-app-routes route-id {:keys [route-template]}]
                      (vector/conj-item reserved-app-routes route-template))
                  (param [])
                  (param routes))))

(defn get-reserved-server-routes
  ; @return (strings in vector)
  [db _]
  (get-in db (db/meta-item-path ::routes :reserved-server-routes)))

(defn get-reserved-routes
  ; @return (strings in vector)
  [db _]
  (let [reserved-app-routes    (r get-reserved-app-routes    db)
        reserved-server-routes (r get-reserved-server-routes db)]
       (vector/concat-items reserved-app-routes reserved-server-routes)))

(a/reg-sub :x.app-router/get-reserved-routes get-reserved-routes)

(defn get-router-data
  ; @return (map)
  [db _]
  {:route-string       (r get-current-route-string       db)
   :route-id           (r get-current-route-id           db)
   :route-path         (r get-current-route-path         db)
   :route-template     (r get-current-route-template     db)
   :route-path-params  (r get-current-route-path-params  db)
   :route-query-params (r get-current-route-query-params db)
   :route-fragment     (r get-current-route-fragment     db)})

(a/reg-sub :x.app-router/get-router-data get-router-data)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-current-route-string!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-string]]
  (-> db (assoc-in (db/meta-item-path ::routes :route-string) route-string)
         (assoc-in (db/meta-item-path ::routes :debug) [:x.app-router/get-router-data])))

(defn- store-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [event-id route-string]]
  (r store-current-route-string! db route-string))

(a/reg-event-db ::store-current-route! store-current-route!)

(defn- set-default-routes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db (db/path ::routes) DEFAULT-ROUTES))

(defn- set-home!
  ; @param (map) route-props
  ;  {:on-leave-event (metamorphic-event)(opt)
  ;   :restricted? (boolean)(opt)
  ;    Default: false
  ;   :route-event (metamorphic-event)
  ;   :route-title (metamorphic-value)(opt)
  ;   :scroll-to-fragment? (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (map)
  [db [_ route-props]]
  (update-in db (db/path ::routes :home) merge route-props))

; @usage
;  [:x.app-router/set-home! {:route-event [:render-homepage!]}]
(a/reg-event-db :x.app-router/set-home! set-home!)

(defn- add-route!
  ; @param (keyword)(opt) route-id
  ; @param (map) route-props
  ;  {:on-leave-event (metamorphic-event)(opt)
  ;   :restricted? (boolean)(opt)
  ;    Default: false
  ;   :route-event (metamorphic-event)
  ;   :route-template (string)
  ;    Not allowed: {:route-template "/"}
  ;   :route-title (metamorphic-value)(opt)
  ;   :scroll-to-fragment? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  (r router/add-route! {...})
  ;
  ; @usage
  ;  (r router/add-route! :my-route {...})
  ;
  ; @return (map)
  [db event-vector]
  (let [route-id    (a/event-vector->second-id   event-vector)
        route-props (a/event-vector->first-props event-vector)]
       (assoc-in db (db/path ::routes route-id) route-props)))

; @usage
;  [:x.app-router/add-route! {...}]
;
; @usage
;  [:x.app-router/add-route! :my-route {...}]
;
; @usage
;  [:x.app-router/add-route!
;   ::route
;   {:on-leave-event [::do-something-else!]
;    :route-event    [::do-something!]
;    :route-template "/do-something"}
(a/reg-event-db :x.app-router/add-route! add-route!)

(defn- reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (r db/apply! db (db/meta-item-path ::routes :history) vector/conj-item route-id))

(a/reg-event-db ::reg-to-history! reg-to-history!)

(defn- freeze-current-route-string!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (assoc-in db (db/meta-item-path ::routes :freezed-route-string)
                    (param current-route-string))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::scroll-to-fragment?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  (fn [{:keys [db]} [_ route-id]]
      (if (r scroll-to-fragment? db route-id)
          (let [route-fragment (r get-current-route-fragment db)
                dom-id         (fragment-id->dom-id route-fragment)]
               [:x.app-environment.scroll-handler/scroll-to-element-top!
                dom-id SCROLL-TO-FRAGMENT-OFFSET]))))

(a/reg-event-fx
  ::dispatch-on-leave-event?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  (fn [{:keys [db]} [_ route-id]]
      {:dispatch-some (r get-on-leave-event db route-id)}))

(a/reg-event-fx
  ::dispatch-route-event?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  (fn [{:keys [db]} [_ route-id]]
      (if (r require-authentication? db route-id)
          {:dispatch-n [[:x.app-views.login-box/render! {:render-exclusive? true}]
                        [:x.boot-loader/set-restart-target! (r get-current-route-string db)]]}
          {:dispatch-some (r get-route-event db route-id)})))

(a/reg-event-fx
  ::set-route-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  (fn [{:keys [db]} [_ route-id]]
      (if-let [route-title (r get-route-prop db route-id :route-title)]
              [:x.app-ui/set-title! route-title]
              [:x.app-ui/restore-default-title!])))

(a/reg-event-fx
  ::handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [_ route-string]]
      (let [route-id          (r get-route-id db route-string)
            previous-route-id (r get-current-route-id db)]
           {; Dispatch on-leave event if ...
            :dispatch-if [(r route-id-changed? db route-id)
                          [::dispatch-on-leave-event?! previous-route-id]]
                         ; Store the current route
            :dispatch-n [[::store-current-route!   route-string]
                         ; Dispatch route-event if exists
                         [::dispatch-route-event?! route-id]
                         ; Set the route title
                         [::set-route-title!       route-id]
                         ; Make history
                         [::reg-to-history!        route-id route-string]]
            ; Scroll to fragment
            :dispatch-later [{:ms SCROLL-TO-FRAGMENT-DELAY
                              :dispatch [::scroll-to-fragment?! route-id]}]})))

(a/reg-event-fx
  :x.app-router/freeze-current-route!
  ; @usage
  ;  [:x.app-router/freeze-current-route!]
  (fn [{:keys [db]} _]
      {:db (r freeze-current-route-string! db)}))

(a/reg-event-fx
  :x.app-router/go-home!
  ; @usage
  ;  [:x.app-router/go-home!]
  (fn [{:keys [db]} _]
      (let [route-template (r get-route-template db :home)]
           [:x.app-router/go-to! route-template])))

(a/reg-event-fx
  :x.app-router/go-back!
  ; @usage
  ;  [:x.app-router/go-back!]
  [::navigate-back!])

(a/reg-event-fx
  :x.app-router/go-to!
  ; @param (string) route-string
  ;
  ; @usage
  ;  [:x.app-router/go-to! "/products/big-green-bong?type=hit#order"]
  (fn [{:keys [db]} [_ route-string]]
      (let [route-string (r get-debug-route-string db route-string)]
           (if (r reload-same-path? db route-string)
               [::handle-route! route-string]
               [::navigate!     route-string]))))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- configure-navigation!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  []
  (accountant/configure-navigation! ACCOUNTANT-CONFIG))

(a/reg-fx ::configure-navigation! configure-navigation!)

(defn- navigate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  [route-string]
  (accountant/navigate! route-string))

(a/reg-handled-fx ::navigate! navigate!)

(defn- navigate-back!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [history (.-history js/window)]
       (.back history)))

(a/reg-handled-fx ::navigate-back! navigate-back!)

(defn- dispatch-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (accountant/dispatch-current!))

(a/reg-handled-fx :x.app-router/dispatch-current-route! dispatch-current-route!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fragment-marker
  ; @param (keyword) fragment-id
  ;
  ; @return (hiccup)
  [fragment-id]
  [:div {:id (fragment-id->dom-id fragment-id)}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::initialize!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
       ; Set default routes
      {:db (r set-default-routes! db)
       ; Configure navigation
       ::configure-navigation! nil}))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [::initialize!]})
