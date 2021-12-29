
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v1.4.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [x.server-core.api :as a :refer [r]]
              [x.server-db.api   :as db]
              [x.server-router.engine     :as engine]
              [x.mid-router.route-handler :as route-handler]))



;; -- WARNING -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az x4.4.6 verzió óta a szerver nem küldi el a kliens számára a server-routes
; útvonalak adatait.
;
; Bizonyos esetekben szükséges lehet a kliens számára megállapítani, hogy egy
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
;  adódnak át az server-route útvonalak és azok adatai:
;  Pl.:
; [["/my-route"   {:get #(my-handler %)}]
;  ["/your-route" {:post {...}}]]
;
; @name ordered-routes
;  A vektorba struktúrált server-route útvonalak a route-template értékük szerint abc sorrendbe
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
; @name server-routes
;  A {:get ...} vagy {:post ...} tulajdonságokat tartalmazó útvonalak, amelyek adatait
;  a szerver átadja a Reitit router számára.
;
; @name client-routes
;  A szerver által az egyes kliensekre elküldött útvonalak.
;  A szerver a {:get ...} vagy {:post ...} tulajdonságokat nem tartalmazó útvonalakat
;  küldi el a klienseknek.
;
; @name {:restricted? true}
;  Az egyes {:restricted? true} tulajdonságú útvonalak kiszolgálása, a {:client-event ...}
;  és a {:server-event ...} események megtörténése a felhasználó azonosításhoz kötött.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler
(def get-app-home route-handler/get-app-home)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-server-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @example
  ;  (r router/get-server-routes db)
  ;  =>
  ;  {:my-route   {:get  {...} :route-template "/my-route"}
  ;   :your-route {:post {...} :route-template "/your-route"}}
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::server-routes)))

; @usage
;  [:router/get-server-routes]
(a/reg-sub :router/get-server-routes get-server-routes)

(defn get-client-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @example
  ;  (r router/get-client-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::client-routes)))

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

(defn- store-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (if (engine/route-props->server-route? route-props)
      (assoc-in db (db/path ::server-routes route-id) route-props)
      (assoc-in db (db/path ::client-routes route-id) route-props)))

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
  [db event-vector]
  (let [event-id    (a/event-vector->first-id    event-vector)
        route-id    (a/event-vector->second-id   event-vector)
        route-props (a/event-vector->first-props event-vector)]
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
(a/reg-event-db :router/add-route! add-route!)

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