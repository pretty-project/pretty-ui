
(ns pretty-elements.stepper.views
    (:require [pretty-elements.stepper.attributes :as stepper.attributes]
              [pretty-elements.stepper.prototypes :as stepper.prototypes]
              [random.api                         :as random]))

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
  ; @warning
  ; UNFINISHED! DO NOT USE!
  ;
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
   (fn [_ stepper-props] ; XXX#0106 (README.md#parametering)
       (let [] ; stepper-props (stepper.prototypes/stepper-props-prototype stepper-props)
            [stepper stepper-id stepper-props]))))
