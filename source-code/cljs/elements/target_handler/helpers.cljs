
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.target-handler.helpers
    (:require [mid-fruits.candy      :refer [param]]
              [hiccup.api            :as hiccup]
              [x.app-environment.api :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-id->target-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Egyes esetekben az elem DOM struktúrája nem teszi lehetővé, hogy az element-id
  ; az elem működését biztosító DOM elemet azonosítsa (pl. input, button, ...)
  ;
  ; @param (keyword) element-id
  ;
  ; @example
  ;  (element-id->target-id :my-namespace/my-element)
  ;  =>
  ;  "my-namespace--my-element--target"
  ;
  ; @return (string)
  [element-id]
  (hiccup/value element-id "target"))

(defn element-id->target-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [element-id]
  (-> element-id element-id->target-id x.environment/element-disabled?))

(defn element-id->target-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [element-id]
  (-> element-id element-id->target-id x.environment/element-enabled?))
