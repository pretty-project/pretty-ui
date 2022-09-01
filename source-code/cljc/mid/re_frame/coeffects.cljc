
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.coeffects
    (:require [mid.re-frame.core :as core]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.core
(def inject-cofx core/inject-cofx)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cofx->event-vector
  ; @param (map) cofx
  ;  {:event (vector)}
  ;
  ; @example
  ;  (re-frame/cofx->event-vector {:event [...]})
  ;  =>
  ;  [...]
  ;
  ; @return (vector)
  [cofx]
  (get cofx :event))

(defn cofx->event-id
  ; @param (map) cofx
  ;  {:event (vector)
  ;    [(keyword) event-id]}
  ;
  ; @example
  ;  (re-frame/cofx->event-vector {:event [:my-event ...]})
  ;  =>
  ;  :my-event
  ;
  ; @return (keyword)
  [cofx]
  (get-in cofx [:event 0]))
