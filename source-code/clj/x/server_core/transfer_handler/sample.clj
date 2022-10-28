
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.transfer-handler.sample
    (:require [re-frame.api      :as r]
              [x.server-core.api :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A my-data-f függvény visszatési értéke a kliens-oldali applikáció indulásakor
; letöltődik és a target-path útvonalon kiolvasható lesz a kliens-oldali
; Re-Frame adatbázisból.
(defn my-data-f
  [request]
  {:my-data "..."})

(x.core/reg-transfer! :my-transfer
  {:data-f      my-data-f
   :target-path [:my-data]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A reg-transfer! függvényt lehetséges Re-Frame mellékhatás eseményként is meghívni.
(defn your-data-f
  [request]
  {:our-data "..."})

(r/reg-event-fx :reg-your-transfer!
  {:core/reg-transfer! [:your-transfer {:data-f      your-data-f
                                        :target-path [:your-data]}]})
