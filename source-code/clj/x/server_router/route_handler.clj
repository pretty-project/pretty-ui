
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v1.2.6
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler
    (:require [mid-fruits.candy       :refer [param return]]
              [mid-fruits.map         :as map]
              [mid-fruits.string      :as string]
              [mid-fruits.vector      :as vector]
              [x.server-core.api      :as a :refer [r]]
              [x.server-db.api        :as db]
              [x.server-router.engine :as engine]))



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
;  ["/my-route" {:get #(my-handler %) :post #(your-handler %)}]
;
; @name route-template
;  Pl.:
;  "/my-route"
;
; @name route-props
;  Pl.:
;  {:get            #(my-handler %)
;   :post           {...}
;   :js             "app.js"
;   :client-event   [:do-something-on-client!]
;   :server-event   [:do-something-on-server!]
;   :restricted?    true
;   :route-template "/my-route"
;   :route-title    "My route"}
;
; @name structured-routes
;  Az útvonalak és azok adatai route-id - route-props kulcs-érték párokként
;  tárolódnak a szerver-oldali Re-Frame adatbázisban:
;  Pl.:
;  {:my-route    {:route-template "/my-route"
;                 :get {...}}
;   :your-route {:route-template "/your-route"
;                :post {...}}}
;
; @name destructed-routes
;  Az applikáció indításakor a Reitit router számára egy vektorba struktúrálva
;  adódnak át az útvonalak és azok adatai:
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
; @name client-routes
;  A szerver által az egyes kliensekre elküldött útvonalak, amelykből a szerveroldali
;  route-tulajdonságok eltávolításra kerülnek.
;  A szerver a {:client-event ...} és/vagy {:on-leave-event ...} tulajdonságú útvonalakat
;  küldi el a klienseknek.
;
; @name server-routes
;  A {:client-event ...} vagy {:on-leave-event ...} tulajdonságot nem tartalmazó útvonalak,
;  amelyek adatait a szerver nem küldi el az egyes klienseknek.
;
; @name restricted-route
;  Az egyes restricted útvonalak kiszolgálása, a :client-event és :server-event események
;  megtörténése felhasználó azonosításhoz kötött.
;
; @name route-parent
;  A route-parent útvonalat a kliens-oldali applikáció a "Vissza" gomb célpontjaként alkalmazza.
;
; @name route-title
;  ...
;
; @name restricted?
;  A {:restricted? true} tulajdonságú útvonalak az applikáció belső útvonalai, amelyek
;  használata kizárólag azonosított felhasználók számára lehetséges.
;
;  Ha az x.project-config.edn fájlban az :app-home értéke pl. "/my-app"
;  akkor a "/my-app/my-route" útvonalon érheted el a {:restricted? true} beállítás
;  használatával hozzáadott útvonalat, mivel azt applikáció-útvonalként fogja azt kezelni.



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az egyes útvonalakhoz tartozó beállításokban az route-template tulajdonságnak relatív útvonalat
;  szükséges átadni.
;  Pl.: {:route-template "/my-route"}
;
; @description
;  A {:restricted? true} tulajdonságú útvonalak applikáció módban, a {:restricted? false}
;  tulajdonságú útvonalak pedig weboldal módban indítják el a kliens-oldali útvonalkezelőt.
;
; @description
;  Az applikáció módban elindított kliens-oldali útvonalkezelő eltávolítja az applikáció elérési
;  útvonalát az útvonal elejéről, hogy az így kapott relatív útvonalhoz tartozó útvonal-beállításokat
;  alkalmazhassa.
;  Pl.:
;  app-home:                      "/admin"
;  böngészőben meghívott útvonal: "/admin/my-route"
;  relatív-útvonal:               "/my-route"



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (a/reg-lifecycles
;   ::lifecycles
;   {:on-app-boot {:dispatch-n [[:router/add-route! {...}]
;                               [:router/add-route! :my-route {...}]
;                               [:router/add-route! :page-not-found {...}]]}})
;
; @usage
;  x.project-config.edn: {:app-details {:app-home "/admin"}}
;  (a/reg-lifecycles
;   ::lifecycles
;   {:on-app-boot {:dispatch-n [[:router/add-route! {:route-template "/my-route"}]
;                               [:router/add-route! {:route-template "/your-route"
;                                                    :restricted?    true}]]})
;
;  (a/dispatch [:router/go-to! "/my-route"])     =>   "/my-route" útvonalra vezet
;  (a/dispatch [:router/go-to! "/your-route"])   =>   "/admin/your-route" útvonalra vezet
;
; @usage
;  (a/dispatch [:router/add-route! {:client-event   [:render-my-view!]
;                                   :route-template "/my-route"
;                                   :route-parent   "/home-page"}])



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
;  Az egyes útvonalak mely tulajdonságait küldje el a szerver a klienseknek
(def CLIENT-ROUTE-PARAMS
     [:client-event :on-leave-event :restricted? :route-parent :route-template :route-title])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- route-props->client-route?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-props
  ;  {:client-event (metamorphic-event)(opt)
  ;   :on-leave-event (metamorphic-event)(opt)}
  ;
  ; @return (boolean)
  [{:keys [client-event on-leave-event]}]
  (or (some? client-event)
      (some? on-leave-event)))

(defn- route-props->route-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-props
  ;  {:get (function or map)
  ;   :post (function or map)
  ;   :route-template (string)}
  ;
  ; @example
  ;  (route-props->route-data {:route-template "/my-route" :get (fn [request] ...)})
  ;  =>
  ;  ["/my-route" {:get (fn [request] ...)}]
  ;
  ; @return (vector)
  ;  [(string) route-template
  ;   (map) route-props
  ;     {:get (function or map)
  ;      :post (function or map)}]
  [{:keys [route-template] :as route-props}]
  [route-template (dissoc route-props :route-template)])

(defn- routes->destructed-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (routes->destructed-routes {:my-route   {:route-template "/my-route"   :get (fn [request] ...)}
  ;                              :your-route {:route-template "/your-route" :get (fn [request] ...)}})
  ;  =>
  ;  [["/my-route"   {:get (fn [request] ...)}]]
  ;   ["/your-route" {:get (fn [request] ...)}]]
  ;
  ; @return (vectors in vector)
  [routes]
  (reduce-kv (fn [destructed-routes route-id {:keys [route-template] :as route-props}]
                 (if (engine/route-conflict? destructed-routes route-template)
                     (return destructed-routes)
                     (let [route-data (route-props->route-data route-props)]
                          (vector/conj-item destructed-routes route-data))))
             (param [])
             (param routes)))

(defn- destructed-routes->ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in vector) routes
  ;
  ; @example
  ;  (destructed-routes->ordered-routes [["/my-route"   {:get (fn [request] ...)}]
  ;                                      ["/your-route" {:get (fn [request] ...)}]
  ;                                      ["/our-route"  {:get (fn [request] ...)}]]
  ;  =>
  ;  [["/my-route"   {:get (fn [request] ...)}]]
  ;   ["/our-route"  {:get (fn [request] ...)}]
  ;   ["/your-route" {:get (fn [request] ...)}]]
  ;
  ; @example
  ;  (destructed-routes->ordered-routes [["/my-route/:a" {...}]
  ;                                      ["/my-route/c"  {...}]
  ;                                      ["/my-route/b"  {...}]
  ;                                      ["/my-route"    {...}]]
  ;  =>
  ;  [["/my-route/b"  {...}]]
  ;   ["/my-route/c"  {...}]
  ;   ["/my-route/:a" {...}]
  ;   ["/my-route"    {...}]]
  ;
  ; @return (vectors in vector)
  [destructed-routes]
  (vector/order-items-by destructed-routes engine/route-templates-ordered? first))



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
       (routes->destructed-routes server-routes)))

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
       (destructed-routes->ordered-routes destructed-routes)))

; @usage
;  [:router/get-ordered-routes]
(a/reg-sub :router/get-ordered-routes get-ordered-routes)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-server-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (assoc-in db (db/path ::server-routes route-id) route-props))

(defn- add-client-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (let [client-props (map/inherit route-props CLIENT-ROUTE-PARAMS)]
       (assoc-in db (db/path ::client-routes route-id) client-props)))

(defn add-route!
  ; @param (keyword)(opt) route-id
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;    Default: #(http/html-wrap {:body (views/main %)})
  ;   :post (function or map)(opt)
  ;   :js (string)(opt)
  ;    Default: "app.js"
  ;   :restricted? (boolean)(opt)
  ;    Default: false
  ;   :route-parent (string)(opt)
  ;   :route-template (string)
  ;   :route-title (metamorphic-content)(opt)
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
       (if (route-props->client-route? route-props)
           (-> db (add-server-route! [event-id route-id route-props])
                  (add-client-route! [event-id route-id route-props]))
           (r add-server-route! db route-id route-props))))

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
                 ; Az add-route! függvény az egyes útvonalak hozzáadásakor esetlegesen eltárolja
                 ; azokat transit-route útvonalként, ezért szükséges egyesével elvégezni
                 ; az egyes útvonalak hozzáadását.
                 (r add-route! db route-id route-props))
             (param db)
             (param routes)))

; @usage
;  [:router/add-routes! {:my-route {:route-template "/my-route"
;                                   :get {:handler my-handler}}
;                        :your-route {...}}]
(a/reg-event-db :router/add-routes! add-routes!)
