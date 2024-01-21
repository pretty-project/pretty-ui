
(ns pretty-elements.blank.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-body-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ blank-props]
  (-> {:class :pe-blank--body}
      (pretty-build-kit/border-attributes       blank-props)
      (pretty-build-kit/color-attributes        blank-props)
      (pretty-build-kit/element-size-attributes blank-props)
      (pretty-build-kit/indent-attributes       blank-props)
      (pretty-build-kit/style-attributes        blank-props)))

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
      (pretty-build-kit/class-attributes        blank-props)
      (pretty-build-kit/outdent-attributes      blank-props)
      (pretty-build-kit/state-attributes        blank-props)
      (pretty-build-kit/wrapper-size-attributes blank-props)))
