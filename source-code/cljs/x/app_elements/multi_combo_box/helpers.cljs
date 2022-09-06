
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box.helpers
    (:require [mid-fruits.keyword        :as keyword]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-id->group-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ;
  ; @example
  ;  (multi-combo-box.helpers/box-id->group-id :my-multi-combo-box)
  ;  =>
  ;  :my-multi-combo-box--chip-group
  ;
  ; @return (keyword)
  [box-id]
  (keyword/append box-id :chip-group "--"))

(defn box-id->field-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ;
  ; @example
  ;  (multi-combo-box.helpers/box-id->field-id :my-multi-combo-box)
  ;  =>
  ;  :my-multi-combo-box--text-field
  ;
  ; @return (keyword)
  [box-id]
  (keyword/append box-id :text-field "--"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [box-id box-props]
  (merge (engine/element-default-attributes box-id box-props)
         (engine/element-indent-attributes  box-id box-props)
         {}))