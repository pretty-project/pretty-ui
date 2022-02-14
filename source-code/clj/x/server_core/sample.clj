
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

(a/reg-transfer! :my-transfer {:data-f my-data-f})

(defn your-data-f
  [request]
  ; A your-data-f függvény visszatési értéke a kliens-oldali applikáció indulásakor
  ; letöltődik és a [:your :data] útvonalon eltárolódik a kliens-oldali Re-Frame adatbázisban.
  {:your-data "..."})

(a/reg-transfer! :your-transfer {:data-f your-data-f :target-path [:your :data]})

(defn our-data-f
  [request]
  ; A reg-transfer! függvényt lehetséges Re-Frame mellékhatás eseményként is meghívni.
  {:our-data "..."})

(a/reg-event-fx
  :reg-our-transfer!
  {:core/reg-transfer! [:our-transfer {:data-f our-data-f}]})
