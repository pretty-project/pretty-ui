
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.react.references
    (:require [plugins.react.state :as state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-reference
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  (get-reference :my-element)
  ;
  ; @return (function)
  [element-id]
  (get @state/REFERENCES element-id))

(defn set-reference!
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  [:div {:ref (set-reference! :my-element)}]
  ;
  ; @return (function)
  [element-id]
  #(swap! state/REFERENCES assoc element-id %))
