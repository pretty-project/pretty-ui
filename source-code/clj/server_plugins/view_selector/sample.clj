
(ns server-plugins.view-selector.sample
    (:require [x.server-core.api :as a]
              [server-plugins.view-selector.api :as view-selector]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  ; Az [:view-selector/initialize! ...] esemény hozzáadja a "/@app-home/my-extension"
  ; és "/@app-home/my-extension/:view-id" útvonalakat a rendszerhez, amely útvonalak
  ; használatával betöltődik a kliens-oldalon a view-selector plugin.
  {:on-server-boot [:view-selector/initialize! :my-extension]})

(a/reg-lifecycles
  ::lifecycles
  ; A {:default-view-id ...} beállítás használatával az "/@app-home/my-extension" útvonalon
  ; a kliens-oldali (r get-selected-view db :my-extension) függvény visszatérési értéke
  ; a beállított {:default-view-id ...} értéke lesz.
  {:on-server-boot [:view-selector/initialize! :my-extension {:default-view-id :my-view}]})

(a/reg-lifecycles
  ::lifecycles
  ; Ha a kiválasztott view-id értéke nem található meg az allowed-view-ids felsorolásban,
  ; akkor behelyettesítésre kerül a default-view-id értékével.
  {:on-server-boot [:view-selector/initialize! :my-extension {:default-view-id :my-view
                                                              :allowed-view-ids [:my-view :your-view :our-view]}]})
