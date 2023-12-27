
(ns pretty-elements.blank.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-body-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as blank-props}]
  (-> {:class :pe-blank--body
       :style style}
      (pretty-css/border-attributes       blank-props)
      (pretty-css/color-attributes        blank-props)
      (pretty-css/element-size-attributes blank-props)
      (pretty-css/indent-attributes       blank-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ blank-props]
  (-> {:class :pe-blank}
      (pretty-css/class-attributes        blank-props)
      (pretty-css/outdent-attributes      blank-props)
      (pretty-css/state-attributes        blank-props)
      (pretty-css/wrapper-size-attributes blank-props)))
