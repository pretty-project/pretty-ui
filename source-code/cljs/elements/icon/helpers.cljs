
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:color (keyword)
  ;  icon-family (keyword)
  ;  layout (keyword)
  ;  size (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-color (keyword)
  ;  :data-icon-family (keyword)
  ;  :data-layout (keyword)
  ;  :data-size (keyword)
  ;  :style (map)}
  [_ {:keys [color icon-family layout size style]}]
  (-> {:style style}
      (element.helpers/apply-color :color :data-color color)
      (merge {:data-icon-family icon-family
              :data-layout      layout
              :data-selectable  false
              :data-size        size})))

(defn icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  [icon-id icon-props]
  (merge (element.helpers/element-default-attributes icon-id icon-props)
         (element.helpers/element-indent-attributes  icon-id icon-props)))
