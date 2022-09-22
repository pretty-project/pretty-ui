
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-marker.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-color-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ; @param (keyword or string) color
  ;
  ; @return (map)
  ;  {:data-color (keyword)
  ;   :style (map)}
  [_ _ color]
  (element.helpers/apply-color {} :background-color :data-color color))

(defn marker-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;
  ; @return (map)
  [_ {:keys [style]}]
  {:style style})

(defn marker-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;  {:size (keyword)}
  ;
  ; @return (map)
  ;  {:data-size (keyword)}
  [marker-id {:keys [size] :as marker-props}]
  (merge (element.helpers/element-default-attributes marker-id marker-props)
         (element.helpers/element-indent-attributes  marker-id marker-props)
         {:data-size size}))
