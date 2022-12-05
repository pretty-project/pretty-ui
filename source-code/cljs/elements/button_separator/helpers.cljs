
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.button-separator.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:color (keyword or string)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [color style]}]
  (-> {:style style}
      (element.helpers/apply-color :color :data-color color)))

(defn separator-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  [separator-id separator-props]
  (merge {}
         (separator-style-attributes separator-id separator-props)))

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  [separator-id separator-props]
  (merge (element.helpers/element-default-attributes separator-id separator-props)
         (element.helpers/element-indent-attributes  separator-id separator-props)))
