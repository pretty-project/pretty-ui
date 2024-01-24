
(ns pretty-elements.stepper.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ stepper-props]
  (-> {:class :pe-stepper--body}
      (pretty-css/indent-attributes stepper-props)
      (pretty-css/style-attributes  stepper-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-attributes
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ stepper-props]
  (-> {:class :pe-stepper}
      (pretty-css/class-attributes   stepper-props)
      (pretty-css/outdent-attributes stepper-props)
      (pretty-css/state-attributes   stepper-props)))
