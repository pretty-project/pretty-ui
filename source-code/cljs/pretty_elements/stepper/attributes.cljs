
(ns pretty-elements.stepper.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as stepper-props}]
  (-> {:class :pe-stepper--body
       :style style}
      (pretty-css/indent-attributes stepper-props)))

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
      (pretty-css/default-attributes stepper-props)
      (pretty-css/outdent-attributes stepper-props)))
