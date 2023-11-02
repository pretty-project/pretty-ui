
(ns elements.horizontal-separator.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-body-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [_ {:keys [style] :as separator-props}]
  (-> {:class           :pe-horizontal-separator--body
       :data-selectable false
       :style           style}
      (pretty-css/color-attributes  separator-props)
      (pretty-css/indent-attributes separator-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ separator-props]
  (-> {:class :pe-horizontal-separator}
      (pretty-css/default-attributes      separator-props)
      (pretty-css/outdent-attributes      separator-props)
      (pretty-css/element-size-attributes separator-props)))
