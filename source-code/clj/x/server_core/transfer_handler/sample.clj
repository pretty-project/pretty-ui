

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.transfer-handler.sample
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A my-data-f függvény visszatési értéke a kliens-oldali applikáció indulásakor
; letöltődik és a :my-transfer azonosítóval kiolvasható lesz a kliens-oldali
; Re-Frame adatbázisból.
(defn my-data-f
  [request]
  {:my-data "..."})

(a/reg-transfer! :my-transfer {:data-f my-data-f})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A your-data-f függvény visszatési értéke a kliens-oldali applikáció indulásakor
; letöltődik és a [:your :data] útvonalon eltárolódik a kliens-oldali Re-Frame adatbázisban.
(defn your-data-f
  [request]
  {:your-data "..."})

(a/reg-transfer! :your-transfer {:data-f your-data-f :target-path [:your :data]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A reg-transfer! függvényt lehetséges Re-Frame mellékhatás eseményként is meghívni.
(defn our-data-f
  [request]
  {:our-data "..."})

(a/reg-event-fx
  :reg-our-transfer!
  {:core/reg-transfer! [:our-transfer {:data-f our-data-f}]})
