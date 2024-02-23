
(ns pretty-elements.stepper.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ stepper-props]
  (-> {:class :pe-stepper--body}
      (pretty-attributes/indent-attributes     stepper-props)
      (pretty-attributes/inner-size-attributes stepper-props)
      (pretty-attributes/style-attributes      stepper-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-attributes
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ stepper-props]
  (-> {:class :pe-stepper}
      (pretty-attributes/class-attributes      stepper-props)
      (pretty-attributes/outdent-attributes    stepper-props)
      (pretty-attributes/outer-size-attributes stepper-props)
      (pretty-attributes/state-attributes      stepper-props)
      (pretty-attributes/theme-attributes      stepper-props)))
