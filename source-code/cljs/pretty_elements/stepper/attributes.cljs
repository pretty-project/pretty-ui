
(ns pretty-elements.stepper.attributes
    (:require [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]))

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
      (pretty-css.layout/indent-attributes stepper-props)
      (pretty-css.basic/style-attributes  stepper-props)))

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
      (pretty-css.basic/class-attributes   stepper-props)
      (pretty-css.layout/outdent-attributes stepper-props)
      (pretty-css.basic/state-attributes   stepper-props)
      (pretty-css.appearance/theme-attributes   stepper-props)))
