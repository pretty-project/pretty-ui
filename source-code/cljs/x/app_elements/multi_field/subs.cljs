
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-field.subs
    (:require [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))

 

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-group-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (strings in vector)
  [db [_ group-id group-props]]
  (r engine/get-input-group-value db group-id group-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :elements.multi-field/get-group-value get-group-value)
