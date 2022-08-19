
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.metamorphic
    (:require [mid-fruits.candy :refer [return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector->effects-map
  ; @param (vector) event-vector
  ;
  ; @usage
  ;  (re-frame/event-vector->effects-map [...])
  ;  =>
  ;  {:dispatch [...]}
  ;
  ; @return (map)
  [event-vector]
  {:dispatch event-vector})

(defn event-vector->handler-f
  ; @param (vector) event-vector
  ;
  ; @usage
  ;  (re-frame/event-vector->handler-f [...])
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [event-vector]
  (fn [_ _] {:dispatch event-vector}))

(defn effects-map->handler-f
  ; @param (map) effects-map
  ;
  ; @usage
  ;  (re-frame/effects-map->handler-f {:dispatch [...]})
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [effects-map]
  (fn [_ _] effects-map))

(defn metamorphic-handler->handler-f
  ; @param (metamorphic-event) n
  ;
  ; @usage
  ;  (re-frame/metamorphic-handler->handler-f [...])
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @usage
  ;  (re-frame/metamorphic-handler->handler-f {:dispatch [...]})
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @usage
  ;  (re-frame/metamorphic-handler->handler-f (fn [_ _] ...))
  ;  =>
  ;  (fn [_ _] ...})
  ;
  ; @return (function)
  [n]
  (cond (map?    n) (effects-map->handler-f  n)
        (vector? n) (event-vector->handler-f n)
        :else       (return                  n)))

(defn metamorphic-event->effects-map
  ; @param (metamorphic-effects) n
  ;
  ; @example
  ;  (re-frame/metamorphic-event->effects-map [:my-event])
  ;  =>
  ;  {:dispatch [:my-event]}
  ;
  ; @example
  ;  (re-frame/metamorphic-event->effects-map {:dispatch [:my-event])
  ;  =>
  ;  {:dispatch [:my-event]}
  ;
  ; @return (map)
  [n]
  (cond (vector? n) (event-vector->effects-map n)
        (map?    n) (return                    n)))
