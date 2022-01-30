
(ns x.server-core.sample
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-data-f
  [request]
  ; A my-data-f függvény visszatési értéke a kliens-oldali applikáció indulásakor
  ; letöltődik és a :my-transfer azonosítóval kiolvasható lesz a kliens-oldali
  ; Re-Frame adatbázisból.
  {:my-data "..."})

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init [:core/reg-transfer! :my-transfer {:data-f my-data-f}]})

(defn your-data-f
  [request]
  ; A your-data-f függvény visszatési értéke a kliens-oldali applikáció indulásakor
  ; letöltődik és a [:your :data] útvonalon eltárolódik a kliens-oldali Re-Frame adatbázisban.
  {:your-data "..."})

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init [:core/reg-transfer! {:data-f your-data-f :target-path [:your :data]}]})
