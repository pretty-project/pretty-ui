
; WARNING! HASN'T FINISHED! DO NOT USE!

(ns elements.stepper.views
    (:require [elements.stepper.attributes :as stepper.attributes]
              [elements.stepper.prototypes :as stepper.prototypes]
              [random.api                  :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- stepper
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  [stepper-id stepper-props]
  [:div (stepper.attributes/stepper-attributes stepper-id stepper-props)
        [:div (stepper.attributes/stepper-body-attributes stepper-id stepper-props)]])

(defn element
  ; @param (keyword)(opt) stepper-id
  ; @param (map) stepper-props
  ; {}
  ;
  ; @usage
  ; [stepper {...}]
  ;
  ; @usage
  ; [stepper :my-stepper {...}]
  ([stepper-props]
   [element (random/generate-keyword) stepper-props])

  ([stepper-id stepper-props]
   (let [] ; stepper-props (stepper.prototypes/stepper-props-prototype stepper-props)
        [stepper stepper-id stepper-props])))
