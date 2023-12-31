
(ns pretty-elements.digit-field.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as field-props}]
  (-> {:class :pe-digit-field--body
       :style style}
      (pretty-build-kit/indent-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [_ field-props]
  (-> {:class :pe-digit-field}
      (pretty-build-kit/class-attributes   field-props)
      (pretty-build-kit/outdent-attributes field-props)
      (pretty-build-kit/state-attributes   field-props)))
