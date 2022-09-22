
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-stamp.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stamp-color-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {}
  ; @param (keyword or string) color
  ;
  ; @return (map)
  ;  {}
  [_ stamp-props color]
  (element.helpers/apply-color {} :background-color :data-color color))

(defn stamp-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:style style})

(defn stamp-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [stamp-id {:keys [size] :as stamp-props}]
  (merge (element.helpers/element-default-attributes stamp-id stamp-props)
         (element.helpers/element-indent-attributes  stamp-id stamp-props)
         {:data-size size}))
