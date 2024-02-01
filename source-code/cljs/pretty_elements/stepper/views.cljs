
(ns pretty-elements.stepper.views
    (:require [fruits.random.api                  :as random]
              [pretty-elements.stepper.attributes :as stepper.attributes]
              [pretty-elements.stepper.prototypes :as stepper.prototypes]
              [pretty-presets.engine.api                  :as pretty-presets.engine]
              [pretty-elements.engine.api                  :as pretty-elements.engine]
              [reagent.api                        :as reagent]))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  [stepper-id stepper-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    stepper-id stepper-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount stepper-id stepper-props))
                       :reagent-render         (fn [_ stepper-props] [stepper stepper-id stepper-props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
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
   [view (random/generate-keyword) stepper-props])

  ([stepper-id stepper-props]
   ; @note (tutorials#parameterizing)
   (fn [_ stepper-props]
       (let [stepper-props (pretty-presets.engine/apply-preset         stepper-id stepper-props)
             stepper-props (stepper.prototypes/stepper-props-prototype stepper-id stepper-props)]
            [view-lifecycles stepper-id stepper-props]))))
