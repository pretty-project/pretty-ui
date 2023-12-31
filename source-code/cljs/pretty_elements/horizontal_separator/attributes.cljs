
(ns pretty-elements.horizontal-separator.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/color-attributes        separator-props)
      (pretty-build-kit/indent-attributes       separator-props)
      (pretty-build-kit/element-size-attributes separator-props)))

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
      (pretty-build-kit/class-attributes        separator-props)
      (pretty-build-kit/outdent-attributes      separator-props)
      (pretty-build-kit/state-attributes        separator-props)
      (pretty-build-kit/wrapper-size-attributes separator-props)))
