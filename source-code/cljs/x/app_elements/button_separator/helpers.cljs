
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button-separator.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [separator-id {:keys [color] :as separator-props}]
  (merge (element.helpers/element-default-attributes separator-id separator-props)
         (element.helpers/element-indent-attributes  separator-id separator-props)
         (element.helpers/apply-color {} :color :data-color color)))
