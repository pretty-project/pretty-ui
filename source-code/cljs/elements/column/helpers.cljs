
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.column.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [gap horizontal-align stretch-orientation style vertical-align wrap-items?]}]
  (cond-> {:data-gap                 gap
           :data-horizontal-align    horizontal-align
           :data-stretch-orientation stretch-orientation
           :data-vertical-align      vertical-align
           :data-wrap-items          wrap-items?
           :style                    style}))

(defn column-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  [column-id column-props]
  (merge (element.helpers/element-default-attributes column-id column-props)
         (element.helpers/element-indent-attributes  column-id column-props)))
