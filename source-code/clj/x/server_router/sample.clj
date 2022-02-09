
(ns x.server-router.sample
    (:require [server-fruits.http :as http]
              [x.server-core.api  :as a]))



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



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  ; A szerver inicializálásakor szükséges beállítani a :method-not-allowed, :not-acceptable
  ; és :not-found alapértelmezett kezelőket!
  {:on-server-init {:dispatch-n [[:router/set-default-route! :method-not-allowed {}]
                                 [:router/set-default-route! :not-acceptable     {}]
                                 [:router/set-default-route! :not-found          {}]]}})

(a/reg-lifecycles!
  ::lifecycles
  ; - A [:router/add-route! ...] eseményeket {:on-server-boot ...} időzítéssel hívd meg!
  ; - A [:router/add-route! ...] eseményeket meghívhatod route-id azonosító megadásával
  ;   vagy anélkül (csak az útvonal tulajdonságait tartalmazó route-props térkép átadásával)
  {:on-server-boot {:dispatch-n [[:router/add-route! {}]
                                 [:router/add-route! :my-route {}]]}})

(a/reg-lifecycles!
  ::lifecycles
  ; Az általad megadott route-id azonosítóval rendelkező útvonalakat könnyebb átlátni
  ; hibakereséskor, illetve lehetséges felülírni a rendszer által hozzáadott útvonalakat,
  ; ha megegyező azonosítóval adsz hozzá útvonalat (pl.: :page-not-found, ...)
  {:on-server-boot [:router/add-route! :page-not-found {}]})

(a/reg-lifecycles!
  ::lifecycles
  ; Ha az x.app-config.edn fájlban az {:app-home "/..."} tulajdonság értékét beállítod
  ; egy tetszőleges útvonalra, akkor az útvonalak {:route-template "/..."} tulajdonságainak
  ; értékében az "/@app-home/..." formula használatával hivatkozhatsz az {:app-home "/..."}
  ; értékére.
  {:on-server-boot {:dispatch-n [[:router/add-route! {:route-template "/my-route"}]
                                 [:router/add-route! {:route-template "/@app-home/your-route"}]]}})

(a/reg-lifecycles!
  ::lifecycles
  ; - A {:route-parent "/..."} tulajdonságként átadott útvonalat tekinti a kliens-oldali útvonal-kezelő
  ;   a hozzáadott útvonal szűlőjének
  ; - A {:route-parent "/..."} tulajdonság használatával az applikáció-módban futó kliens-oldali
  ;   UI felületen a felső [app-header] komponensen a [go-home] gomb helyén a [go-back] gomb
  ;   jelenik meg, amelyre kattintva az útvonal-kezelő a {:route-parent "/..."} tulajdonságként
  ;   átadott útvonalra irányít át.
  ; - Webhely-módban futó kliens-oldali UI felületen nem okoz változást a {:route-parent "/..."}
  ;   tulajdonság használata.
  ; - A [:router/go-up!] kliens-oldali esemény meghívása a {:route-parent "/..."} tulajdonságként
  ;   átadott útvonalra irányít át.
  {:on-server-boot [:router/add-route! {:client-event   [:render-my-view!]
                                        :route-template "/my-route/:my-item-id"
                                        :route-parent   "/my-route"}]})

(a/reg-lifecycles!
  ::lifecycles
  ; A {:js "..."} tulajdonság átadásával az alapbeállítástól eltérő .js fájl is használható.
  {:on-server-boot {:dispatch-n [[:router/add-route! {:route-template "/my-route"
                                                      :get  {:handler #(my-handler %)}
                                                      :js "my-app.js"}]
                                 [:router/add-route! {:route-template "/your-route"
                                                      :post {:handler #(your-handler %)}
                                                      :js "your-app.js"}]]}})

(a/reg-lifecycles!
  ::lifecycles
  ; A {:restricted? true} tulajdonság beállításával az útvonalak kiszolgálása
  ; és az útvonalak eseményeinek lefutásához a felhasználó azonosítása szükséges.
  {:on-server-boot {:dispatch-n [[:router/add-route! {:route-template "/my-route"
                                                      :get  {:handler #(my-handler %)}
                                                      :restricted? true}]
                                 [:router/add-route! {:route-template "/your-route"
                                                      :post {:handler #(your-handler %)}
                                                      :restricted? true}]
                                 [:router/add-route! {:route-template "/our-route"
                                                      :client-event [:render-our-view!]
                                                      :restricted? true}]]}})
