
; WARNING
; Az x.ui.renderer.config névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; x5 Clojure/ClojureScript web application engine
; https://monotech.hu/x5
;
; Copyright Adam Szűcs and other contributors - All rights reserved

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.config)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
; A UI elem animált megjelenítésére rendelkezésre álló idő.
(def REVEAL-ANIMATION-TIMEOUT 350)

; @constant (ms)
; A UI elem animált eltűntetésére rendelkezésre álló idő.
;
; BUG#4701 (def HIDE-ANIMATION-TIMEOUT 350)
(def HIDE-ANIMATION-TIMEOUT 450)

; @constant (ms)
(def UPDATE-ANIMATION-TIMEOUT 350)

; DEBUG
; @constant (ms)
(def RENDER-DELAY-OFFSET 0)
; - Ha a render-delay 100 ms várakozásra volt állítva, akkor az egymás után különböző azonosítóval
;   kirenderelt surface elemek villanva jelentek meg (100 ms különbséggel).
; - Ha pedig különböző surface-ek ugyanazt az azonosítót használták, ami több okból sem jó, akkor
;   nem jelentkezett ez a jelenség, mivel nem történt re-render, csak update-elte a surface a tartalmát.
; - Próbaképpen át lett állítva 0 ms várakozásra 2021. 10. 26.
;   Ha ez nem okoz semmilyen problémát hosszútávon, akkor maradjon az új érték megtartva.
;(def RENDER-DELAY-OFFSET 100)

; @constant (ms)
(def DESTROY-DELAY-OFFSET 100)

; @constant (integer)
(def DEFAULT-MAX-ELEMENTS-RENDERED 32)
