
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.6.0
; Compatibility: x4.4.4



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
;  [:products/render!]
;
; @name route-id
;  :products
;
; @name restricted?
;  A {:restricted? true} tulajdonságú útvonalak az applikáció belső útvonalai, amelyek
;  használata kizárólag azonosított felhasználók számára lehetséges.
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

; Az egyes útvonalakhoz tartozó beállításokban az route-template tulajdonságnak relatív útvonalat
; szükséges átadni.
; Pl.: {:route-template "/my-route"}
;
; A {:restricted? true} tulajdonságú útvonalak applikáció módban, a {:restricted? false}
; tulajdonságú útvonalak pedig weboldal módban indítják el az útvonalkezelőt.
;
; Az applikáció módban elindított útvonalkezelő eltávolítja az applikáció elérési útvonalát
; az útvonal elejéről, hogy az így kapott relatív útvonalhoz tartozó útvonal-beállításokat
; alkalmazhassa.
;
; @usage
;  (a/reg-lifecycles {:on-app-boot {:dispatch-n [[:x.app-router/add-route! {...}]
;                                                [:x.app-router/add-route! :my-route {...}]
;                                                [:x.app-router/add-route! :page-not-found {...}]]}})
;
; @usage
;  x.app-details.edn: {:app-details {:app-home "/admin"}}
;  (a/reg-lifecycles {:on-app-boot {:dispatch-n [[:x.app-router/add-route! {:route-template "/my-route"}]
;                                                [:x.app-router/add-route! {:route-template "/your-route"
;                                                                           :restricted?    true}]]})
;
;  (a/dispatch [:x.app-router/go-to! "/my-route"])     =>   "/my-route" útvonalra vezet
;  (a/dispatch [:x.app-router/go-to! "/your-route"])   =>   "/admin/your-route" útvonalra vezet



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
               :route-template "/reboot"}})

; @constant (map)
(def ACCOUNTANT-CONFIG
     {:nav-handler  #(a/dispatch   [::handle-route!      %])
      :path-exists? #(a/subscribed [::route-path-exists? %])
      :reload-same-path? false})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- valid-route-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-path
  ;
  ; @return (string)
  [route-path]   ; 1.
  (-> route-path (string/not-ends-with! "/")
                 ; 2.
                 (string/starts-with!   "/")))

(defn- fragment-id->dom-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) fragment-id
  ;
  ; @return (hiccup)
  [fragment-id]
  (str "x-fragment-marker--" (name fragment-id)))

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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (let [app-home (r a/get-app-detail db :app-home)]
       (valid-route-path app-home)))

(defn app-home-specific?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [app-home (r get-app-home db)]
       (not= app-home "/")))

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
  [db [_ abs-route-string debug]]
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
  (get-in db (db/meta-item-path ::routes :route-string)))

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
       (get-in db (db/path ::routes current-route-id :route-template))))

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

(defn get-current-route-parent
  ; @usage
  ;  (r router/get-current-route-parent db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db (db/path ::routes current-route-id :route-parent))))



;; -- Freezed route subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

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
       (r match-route-id-by-abs-route-string db freezed-route-string)))

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
  (assoc-in db (db/meta-item-path ::routes :route-string) route-string))

(defn- store-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [event-id route-string]]
  (r store-current-route-string! db route-string))

(defn- set-default-routes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db (db/path ::routes) DEFAULT-ROUTES))

(defn- add-route!
  ; @param (keyword)(opt) route-id
  ; @param (map) route-props
  ;  {:on-leave-event (metamorphic-event)(opt)
  ;   :restricted? (boolean)(opt)
  ;    Default: false
  ;   :route-event (metamorphic-event)
  ;   :route-parent (string)(opt)
  ;   :route-template (string)
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

(defn- freeze-current-route-string!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (assoc-in db (db/meta-item-path ::routes :freezed-route-string)
                    (param current-route-string))))

(defn freeze-current-route!
  ; @return (map)
  [db]
  (r freeze-current-route-string! db))

; @usage
;  [:x.app-router/freeze-current-route!]
(a/reg-event-db :x.app-router/freeze-current-route! freeze-current-route!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::scroll-to-fragment!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  (fn [{:keys [db]} [_ route-id]]
      (let [route-fragment (r get-current-route-fragment db)
            dom-id         (fragment-id->dom-id route-fragment)]
           [:x.app-environment.scroll-handler/scroll-to-element-top!
            dom-id SCROLL-TO-FRAGMENT-OFFSET])))

(a/reg-event-fx
  ::handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  (fn [{:keys [db]} [event-id route-string]]
      (let [route-id          (r match-route-id-by-abs-route-string db route-string :debug)
            previous-route-id (r get-current-route-id db)
            on-leave-event    (r get-on-leave-event   db previous-route-id)]
                       ; Store the current route
           {:db (-> db (store-current-route! [event-id route-string])
                       ; Make history
                       (reg-to-history!      [event-id route-id]))
                         ; Dispatch on-leave event if ...
            :dispatch-n [(if-let [on-leave-event (r get-on-leave-event db previous-route-id)]
                                 (if (r route-id-changed? db route-id) on-leave-event))
                         ; Render login screen if ...
                         (if (r require-authentication? db route-id)
                             [:x.app-views.login-box/render!])
                         ; Set restart-target if ...
                         (if (r require-authentication? db route-id)
                             [:x.boot-loader/set-restart-target! route-string])
                         ; Dispatch route-event if ...
                         (if-let [route-event (r get-route-event db route-id)]
                                 (if-not (r require-authentication? db route-id) route-event))]})))

(a/reg-event-fx
  :x.app-router/go-home!
  ; @usage
  ;  [:x.app-router/go-home!]
  (fn [{:keys [db]} _]
      (let [app-home (r get-app-home db)]
           [:x.app-router/go-to! app-home])))

(a/reg-event-fx
  :x.app-router/go-back!
  ; @usage
  ;  [:x.app-router/go-back!]
  [::navigate-back!])

(a/reg-event-fx
  :x.app-router/go-up!
  ; @usage
  ;  [:x.app-router/go-up!]
  (fn [{:keys [db]} _]
      (let [route-parent (r get-current-route-parent db)]
           [:x.app-router/go-to! route-parent])))

(a/reg-event-fx
  :x.app-router/go-to!
  ; @param (string) rel-route-string
  ;
  ; @usage
  ;  [:x.app-router/go-to! "/products/big-green-bong?type=hit#order"]
  (fn [{:keys [db]} [_ rel-route-string]]
      (let [app-home          (r get-app-home                       db)
            route-id          (r match-route-id-by-rel-route-string db rel-route-string)
            route-restricted? (r route-restricted?                  db route-id)
            abs-route-string  (if route-restricted? (route-string->abs-route-string rel-route-string app-home)
                                                    (param rel-route-string))
            ; Az applikáció az útvonalváltás után is debug módban marad
            abs-route-string (r get-debug-route-string db abs-route-string)]
           (if (r reload-same-path? db abs-route-string)
               [::handle-route! abs-route-string]
               [::navigate!     abs-route-string]))))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- configure-navigation!
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  (fn [{:keys [db]} [event-id]]
                  ; Set default routes
      {:db (-> db (set-default-routes! [event-id DEFAULT-ROUTES])
                  ; Store debug subscriber
                  (assoc-in (db/meta-item-path ::routes :debug) [:x.app-router/get-router-data]))
       ; Configure navigation
       ::configure-navigation! nil}))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [::initialize!]})
