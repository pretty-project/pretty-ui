
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v1.7.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [mid-fruits.uri     :as uri]
              [server-fruits.http :as http]
              [x.server-core.api  :as a :refer [r]]
              [x.server-db.api    :as db]
              [x.mid-router.route-handler :as route-handler]
              [x.server-router.engine     :as engine]
              [x.server-user.api          :as user]))



;; -- WARNING -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az x4.4.6 verzió óta a szerver nem küldi el a kliens számára az útvonalak
; szerver-oldali beállításait.
;
; Bizonyos esetekben szükséges lehet a kliens-oldalon megállapítani, hogy egy
; új útvonalat konfliktus nélkül képes-e hozzáadni a rendszerhez.
; Pl.: új aloldal létrehozásakor.
; Ilyen esetben a hozzáadandó útvonalat szükséges elküldeni a szerver számára,
; hogy az megválaszolja, hogy konfliktus nélkül hozzáadható-e az új útvonal.



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name route-data
;  A Reitit router számára egy kételemű vektorban adódnak át az egyes útvonalak:
;  Pl.:
;  ["/my-route" {:get #(my-handler %)}]
;
; @name route-template
;  Pl.:
;  "/my-route/:my-item-id"
;
; @name route-props
;  Pl.:
;  {:get            #(my-handler %)
;   :post           {...}
;   :js             "app.js"
;   :client-event   [:do-something-on-client!]
;   :server-event   [:do-something-on-server!]
;   :restricted?    true
;   :route-template "/my-route"}
;
; @name structured-routes
;  Az útvonalak és azok adatai route-id - route-props kulcs-érték párokként
;  tárolódnak a szerver-oldali Re-Frame adatbázisban:
;  Pl.:
;  {:my-route   {:route-template "/my-route"
;                :get {...}}
;   :your-route {:route-template "/your-route"
;                :post {...}}}
;
; @name destructed-routes
;  Az applikáció indításakor a Reitit router számára egy vektorba struktúrálva
;  adódnak át a :get és/vagy :post tulajdonsággal rendelkező útvonalak és azok adatai:
;  Pl.:
; [["/my-route"   {:get #(my-handler %)}]
;  ["/your-route" {:post {...}}]]
;
; @name ordered-routes
;  A vektorba struktúrált útvonalak a route-template értékük szerint abc sorrendbe
;  rendezve kerülnek átadásra a Reitit router számára úgy, hogy a path-param
;  változók nevei magasabb értékűnek számítanak – így azok a vektor későbbi elemei,
;  hogy a Reitit router ne kezelje őket konfliktusként:
;  Pl.:
;  [["/my-route"   ...]
;   ["/our-route"  ...]
;   ["/our-route/your-page"]
;   ["/our-route/:my-param"   ...]
;   ["/our-route/:your-param" ...]
;   ["/your-route" ...]]
;
; @name {:restricted? true}
;  Az egyes {:restricted? true} tulajdonságú útvonalak kiszolgálása, a {:client-event ...}
;  és a {:server-event ...} események megtörténése a felhasználó azonosításhoz kötött.
;
; @name client-routes
;  Az egyes útvonalak kliens-oldali tulajdonságai
;
; @name server-routes
;  Az egyes útvonalak szerver-oldali tulajdonságai



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler
(def get-app-home     route-handler/get-app-home)
(def get-resolved-uri route-handler/get-resolved-uri)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def CLIENT-ROUTE-KEYS [:client-event :js :on-leave-event :restricted? :route-parent :route-template])

; @constant (keywords in vector)
(def SERVER-ROUTE-KEYS [:get :js :post :restricted? :route-template :server-event])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->route-prop
  ; @param (map) request
  ; @param (keyword) prop-key
  ;
  ; @usage
  ;  (router/request->route-prop {...} :my-prop)
  ;
  ; @return (*)
  ;  Először az útvonalak szerver-oldali, majd a kliens-oldali tulajdonságain végigiterálva keres
  ;  a route-path értékével összeilleszthető {:route-template ...} tulajdonságú útvonalat,
  ;  ami rendelkezik a prop-key tulajdonságként átadott tulajdonsággal.
  [request prop-key]
  (let [route-path (http/request->route-path request)]
       (letfn [(f [[_ {:keys [route-template] :as route-props}]]
                  (if (uri/path->match-template? route-path route-template) (get route-props prop-key)))]
              (or (some f @(a/subscribe [:router/get-server-routes]))
                  (some f @(a/subscribe [:router/get-client-routes]))))))

(defn- route-authenticator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (function)
  ;
  ; @return (function)
  [handler]
  (fn [request] (if (user/request->authenticated? request)
                    (handler                      request)
                    (http/error-wrap {:body "Access denied" :status 403}))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handler-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (function or map) handler
  ; @param (map) options
  ;  {:restricted? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:handler (function)}
  [handler {:keys [restricted?]}]
  (if restricted? (cond (fn?  handler) (return {:handler (route-authenticator           handler)})
                        (map? handler) (assoc   :handler (route-authenticator (:handler handler))))
                  (cond (fn?  handler) (return {:handler handler})
                        (map? handler) (return handler))))

(defn- route-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)
  ;   :restricted? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:get (map)
  ;   :js (string)
  ;   :post (map)}
  [{:keys [get post restricted?] :as route-props}]
  (merge {:js "app.js"}
         (param route-props)
         (if get  {:get  (handler-prototype get  {:restricted? restricted?})})
         (if post {:post (handler-prototype post {:restricted? restricted?})})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-server-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @usage
  ;  (r router/get-server-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path :router/server-routes)))

; @usage
;  [:router/get-server-routes]
(a/reg-sub :router/get-server-routes get-server-routes)

(defn get-client-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @usage
  ;  (r router/get-client-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path :router/client-routes)))

; @usage
;  [:router/get-client-routes]
(a/reg-sub :router/get-client-routes get-client-routes)

(defn get-destructed-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vectors in vector)
  ;  [[(string) route-template
  ;    (map) route-props
  ;      {:get (function or map)
  ;       :post (function or map)}]
  ;   [...]
  ;   [...]]
  [db _]
  (let [server-routes (r get-server-routes db)]
       (engine/routes->destructed-routes server-routes)))

(defn get-ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @example
  ;  (r router/get-ordered-routes db)
  ;  =>
  ;  [["my-route"  {:get  {...}}]
  ;   ["your-route {:post {...}}"]]
  ;
  ; @return (vectors in vector)
  ;  [[(string) route-template
  ;    (map) route-props
  ;      {:get (function or map)
  ;       :post (function or map)}]
  ;   [...]
  ;   [...]]
  [db _]
  (let [destructed-routes (r get-destructed-routes db)]
       (engine/destructed-routes->ordered-routes destructed-routes)))

; @usage
;  [:router/get-ordered-routes]
(a/reg-sub :router/get-ordered-routes get-ordered-routes)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-server-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)}
  ;
  ; @return (map)
  [db [_ route-id {:keys [get post] :as route-props}]]
  (if (or get post)
      (assoc-in db (db/path :router/server-routes route-id)
                   (select-keys route-props SERVER-ROUTE-KEYS))
      (return   db)))

(defn- store-client-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:client-event (metamorphic-event)(opt)
  ;   :on-leave-event (metamorphic-event)(opt)}
  ;
  ; @return (map)
  [db [_ route-id {:keys [client-event on-leave-event] :as route-props}]]
  (if (or client-event on-leave-event)
      (assoc-in db (db/path :router/client-routes route-id)
                   (select-keys route-props CLIENT-ROUTE-KEYS))
      (return   db)))

(defn- store-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (as-> db % (r store-server-route-props! % route-id route-props)
             (r store-client-route-props! % route-id route-props)))

(defn add-route!
  ; @param (keyword)(opt) route-id
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)
  ;   :js (string)(opt)
  ;    Default: "app.js"
  ;   :restricted? (boolean)(opt)
  ;    Default: false
  ;   :route-parent (string)(opt)
  ;   :route-template (string)
  ;   :client-event (metamorphic-event)(opt)
  ;    Az útvonal meghívásakor a kliens-oldalon megtörténő esemény.
  ;   :on-leave-event (metamorphic-event)(opt)
  ;    Az útvonal elhagyásakor a kliens-oldalon megtörténő esemény.
  ;   :server-event (metamorphic-event)(opt)}
  ;    Az útvonal meghívásakor a szerver-oldalon megtörténő esemény.
  ;
  ; @usage
  ;  (r router/add-route! db {...})
  ;
  ; @usage
  ;  (r router/add-route! db :my-route {...})
  ;
  ; @usage
  ;  (r router/add-route! db :my-route {:route-template "/my-route"
  ;                                     :get (fn [request] ...)})
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (let [route-props (route-props-prototype route-props)]
       (if-let [route-template (get route-props :route-template)]
               ; If route-props contains route-template ...
               (if (engine/variable-route-string? route-template)
                   ; If route-template is variable ...
                   (let [app-home       (r get-app-home db)
                         route-template (engine/resolve-variable-route-string route-template app-home)
                         route-props    (assoc route-props :route-template route-template)]
                        (r store-route-props! db route-id route-props))
                   ; If route-template is static ...
                   (r store-route-props! db route-id route-props))
               ; If route-props NOT contains route-template ...
               (r store-route-props! db route-id route-props))))

; @usage
;  [:router/add-route! {...}]
;
; @usage
;  [:router/add-route! :my-route {...}]
(a/reg-event-db :router/add-route! [a/event-vector<-id] add-route!)

(defn add-routes!
  ; @param (map) routes
  ;
  ; @usage
  ;  (r router/add-routes! db {:my-route {:route-template "/my-route"
  ;                                       :get {:handler my-handler}}
  ;                            :your-route {...}})
  ;
  ; @return (map)
  [db [_ routes]]
  (reduce-kv (fn [db route-id route-props]
                 (r add-route! db route-id route-props))
             (param db)
             (param routes)))

; @usage
;  [:router/add-routes! {:my-route {:route-template "/my-route"
;                                   :get {:handler my-handler}}
;                        :your-route {...}}]
(a/reg-event-db :router/add-routes! add-routes!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer! :router/transfer-client-routes!
                 {:data-f     #(a/subscribed [:router/get-client-routes])
                  :target-path [:router/client-routes :data-items]})
