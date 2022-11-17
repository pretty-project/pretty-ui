
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.sample
    (:require [http.api   :as http]
              [x.core.api :as x.core]))



;; -- WARNING -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az x4.4.6 verzió óta a szerver nem küldi el a kliens számára az útvonalak
; szerver-oldali beállításait.
;
; Bizonyos esetekben szükséges lehet a kliens-oldalon megállapítani, hogy egy
; új útvonalat konfliktus nélkül képes-e hozzáadni a rendszerhez.
; Pl.: Új aloldal létrehozásakor
;      Ilyen esetben a hozzáadandó útvonalat szükséges elküldeni a szerver számára,
;      hogy az megválaszolja, hogy konfliktus nélkül hozzáadható-e az új útvonal.



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
;   :js-build       :app
;   :client-event   [:my-client-event]
;   :server-event   [:my-server-event]
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- my-handler
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (http/map-wrap {:body {}}))

(defn- your-handler
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (http/map-wrap {:body {}}))



;; -- Alapértelmezett kezelők beállítása (kötelező) ---------------------------
;; ----------------------------------------------------------------------------

; A szerver inicializálásakor szükséges beállítani a :method-not-allowed, :not-acceptable
; és :not-found alapértelmezett kezelőket!
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init {:dispatch-n [[:x.router/set-default-route! :method-not-allowed {}]
                                 [:x.router/set-default-route! :not-acceptable     {}]
                                 [:x.router/set-default-route! :not-found          {}]]}})



;; -- Útvonal(ak) hozzáadása --------------------------------------------------
;; ----------------------------------------------------------------------------

; A [:x.router/add-route! ...] eseményeket {:on-server-boot ...} időzítéssel hívd meg!
; A [:x.router/add-route! ...] eseményeket meghívhatod route-id azonosító megadásával
; vagy anélkül (csak az útvonal tulajdonságait tartalmazó route-props térkép átadásával)
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:x.router/add-route! {}]
                                 [:x.router/add-route! :my-route {}]]}})



;; -- Létező útvonalbeállítások felülírása ------------------------------------
;; ----------------------------------------------------------------------------

; Az általad megadott route-id azonosítóval rendelkező útvonalakat könnyebb átlátni
; hibakereséskor, illetve lehetséges felülírni a rendszer által hozzáadott útvonalakat,
; ha megegyező azonosítóval adsz hozzá útvonalat (pl. :page-not-found, ...)
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! :page-not-found {}]})



;; -- "@app-home" hivatkozás használata útvonalakban --------------------------
;; ----------------------------------------------------------------------------

; Ha az x.app-config.edn fájlban az {:app-home "/..."} tulajdonság értékét beállítod
; egy tetszőleges útvonalra, akkor az útvonalak {:route-template "/..."} tulajdonságainak
; értékében az "/@app-home/..." formula használatával hivatkozhatsz az {:app-home "/..."}
; értékére.
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:x.router/add-route! {:route-template "/my-route"}]
                                 [:x.router/add-route! {:route-template "/@app-home/your-route"}]]}})



;; -- {:js-build "..."} tulajdonság használata --------------------------------
;; ----------------------------------------------------------------------------

; A {:js-build "..."} tulajdonság megadásával a rendszer a body komponensben
; elhelyezi a megadott build-et kiszolgáló js fájlt.
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:x.router/add-route! {:route-template "/my-route"
                                                        :get {:handler #(my-handler %)}
                                                        :js-build :my-build}]
                                 [:x.router/add-route! {:route-template "/your-route"
                                                        :post {:handler #(your-handler %)}
                                                        :js-build :your-build}]]}})



;; -- Védett útvonalak --------------------------------------------------------
;; ----------------------------------------------------------------------------

; A {:restricted? true} tulajdonság beállításával az útvonalak kiszolgálása
; és az útvonalak eseményeinek lefutásához a felhasználó azonosítása szükséges.
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:x.router/add-route! {:route-template "/my-route"
                                                        :get  {:handler #(my-handler %)}
                                                        :restricted? true}]
                                 [:x.router/add-route! {:route-template "/your-route"
                                                        :post {:handler #(your-handler %)}
                                                        :restricted? true}]
                                 [:x.router/add-route! {:route-template "/our-route"
                                                        :client-event [:render-our-view!]
                                                        :restricted? true
                                                        :js-build :my-build}]]}})



;; -- Webhelytérkép használata ------------------------------------------------
;; ----------------------------------------------------------------------------

; Az {:add-to-sitemap? true} beállítás használatával az útvonal hozzáadódik
; a /sitemap.xml fájlban kiszolgált webhelytérképhez.
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! {:route-template "/my-route"
                                          :add-to-sitemap? true}]})
