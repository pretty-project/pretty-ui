
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns react.references
    (:require [react.state :as state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-reference
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  (react/get-reference :my-element)
  ;
  ; @return (function)
  [element-id]
  (get @state/REFERENCES element-id))

(defn set-reference!
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  [:div {:ref (react/set-reference! :my-element)}]
  ;
  ; @return (function)
  [element-id]
  #(swap! state/REFERENCES assoc element-id %))
