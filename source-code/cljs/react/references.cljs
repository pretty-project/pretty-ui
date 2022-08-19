
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
