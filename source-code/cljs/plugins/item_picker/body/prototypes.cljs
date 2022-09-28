
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.body.prototypes
    (:require [mid-fruits.candy                 :refer [param]]
              [plugins.item-picker.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:item-path (vector)}
  [picker-id body-props]
  (merge {:item-path (core.helpers/default-item-path picker-id)}
         (param body-props)))
