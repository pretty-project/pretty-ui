
(ns pretty-engine.element.state.side-effects
    (:require [fruits.map.api           :as map]
              [pretty-engine.core.state :as core.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-all-element-state!
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ; (update-all-element-state! dissoc :my-key)
  [f & params]
  (letfn [(f0 [%] (map/->values % f1))
          (f1 [%] (apply f % params))]
         (swap! core.state/STATE f0)))

(defn update-element-state!
  ; @param (keyword) element-id
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ; (update-element-state! :my-element assoc :my-key "My value")
  [element-id f & params]
  (letfn [(f0 [%] (apply f % params))]
         (swap! core.state/STATE update element-id f0)))

(defn clear-element-state!
  ; @param (keyword) element-id
  ;
  ; @usage
  ; (clear-element-state! :my-element)
  [element-id]
  (swap! core.state/STATE dissoc element-id))
