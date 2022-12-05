
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.color-stamp.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stamp-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ; {:size (keyword)
  ;  :style (map)}
  ;
  ; @return (map)
  ; {:data-size (keyword)
  ;  :style (map)}
  [_ {:keys [size style]}]
  {:data-size size
   :style     style})

(defn stamp-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;
  ; @return (map)
  [stamp-id stamp-props]
  (merge (element.helpers/element-default-attributes stamp-id stamp-props)
         (element.helpers/element-indent-attributes  stamp-id stamp-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stamp-color-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ; {}
  ; @param (keyword or string) color
  ;
  ; @return (map)
  ; {}
  [_ stamp-props color]
  (element.helpers/apply-color {} :background-color :data-color color))
