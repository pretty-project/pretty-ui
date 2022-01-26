
(ns server-plugins.view-selector.sample
    (:require [x.server-core.api :as a]
              [server-plugins.view-selector.api :as view-selector]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  ; - Az [:view-selector/initialize-selector! ...] esemény hozzáadja az "/@app-home/my-extension"
  ;   és az "/@app-home/my-extension/:view-id" útvonalakat a rendszerhez, amely útvonalak
  ;   használatával betöltődik a kliens-oldalon a view-selector plugin.
  ; - A {:routed? false} beállítás használatával NEM adja hozzá az útvonalakat.
  {:on-server-boot [:view-selector/initialize-selector! :my-extension]})

(a/reg-lifecycles
  ::lifecycles
  ; A {:default-view-id ...} beállítás használatával az "/@app-home/my-extension" útvonalon,
  ; (ahol az útvonal nem tartalmazza a view-id értékét) a kliens-oldali aktuális view-id
  ; a beállított {:default-view-id ...} értéke lesz, a ... beállítás használata nélkül pedig
  ; a kliens-oldali aktuális view-id értéke nil lesz.
  {:on-server-boot [:view-selector/initialize-selector! :my-extension
                                                        {:default-view-id :my-view}]})

(a/reg-lifecycles
  ::lifecycles
  ; Ha a kliens-oldali aktuális view-id értéke nem található meg az allowed-view-ids felsorolásban,
  ; akkor behelyettesítésre kerül a default-view-id értékével.
  {:on-server-boot [:view-selector/initialize-selector! :my-extension
                                                        {:default-view-id   :my-view
                                                         :allowed-view-ids [:my-view :your-view :our-view]}]})
