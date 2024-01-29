
(ns pretty-elements.stepper.views
    (:require [fruits.random.api                  :as random]
              [pretty-elements.stepper.attributes :as stepper.attributes]
              [pretty-elements.stepper.prototypes :as stepper.prototypes]
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

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  [stepper-id stepper-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    stepper-id stepper-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount stepper-id stepper-props))
                       :reagent-render         (fn [_ stepper-props] [stepper stepper-id stepper-props])}))

(defn element
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
   [element (random/generate-keyword) stepper-props])

  ([stepper-id stepper-props]
   ; @note (tutorials#parametering)
   (fn [_ stepper-props]
       (let [] ; stepper-props (stepper.prototypes/stepper-props-prototype stepper-props)
            [element-lifecycles stepper-id stepper-props]))))
