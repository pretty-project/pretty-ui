
; WARNING! NOT TESTED! DO NOT USE!

(ns elements.stepper.views
    (:require [elements.stepper.helpers    :as stepper.helpers]
              [elements.stepper.prototypes :as stepper.prototypes]
              [random.api                  :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- stepper-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  [stepper-id stepper-props]
  [:div.e-stepper--body (stepper.helpers/stepper-body-attributes stepper-id stepper-props)])

(defn- stepper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  [stepper-id stepper-props]
  [:div.e-stepper (stepper.helpers/stepper-attributes stepper-id stepper-props)
                  [stepper-body                       stepper-id stepper-props]])

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
   (let [] ; stepper-props (stepper.prototypes/stepper-props-prototype stepper-props)
        [stepper stepper-id stepper-props])))
