
(ns pretty-elements.stepper.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/indent-attributes stepper-props)))

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
      (pretty-build-kit/class-attributes   stepper-props)
      (pretty-build-kit/outdent-attributes stepper-props)
      (pretty-build-kit/state-attributes   stepper-props)))
