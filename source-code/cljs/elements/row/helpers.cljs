
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.row.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-body-attributes
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

(defn row-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  [row-id row-props]
  (merge (element.helpers/element-default-attributes row-id row-props)
         (element.helpers/element-indent-attributes  row-id row-props)))
