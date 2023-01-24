
(ns react.references
    (:require [react.state :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-reference
  ; @param (keyword) element-id
  ;
  ; @usage
  ; (get-reference :my-element)
  ;
  ; @return (?)
  [element-id]
  (get @state/REFERENCES element-id))

(defn set-reference-f
  ; @param (keyword) element-id
  ;
  ; @usage
  ; [:div {:ref (set-reference-f :my-element)}]
  ;
  ; @return (function)
  [element-id]
  (fn [ref] (swap! state/REFERENCES assoc element-id ref)))
