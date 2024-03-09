
(ns pretty-elements.stepper.views
    (:require [fruits.random.api                  :as random]
              [pretty-elements.engine.api         :as pretty-elements.engine]
              [pretty-elements.stepper.attributes :as stepper.attributes]
              [pretty-elements.stepper.prototypes :as stepper.prototypes]
              [pretty-elements.methods.api :as pretty-elements.methods]
              [reagent.core                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- stepper
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (stepper.attributes/outer-attributes id props)
        [:div (stepper.attributes/inner-attributes id props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [stepper id props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ;
  ; @usage (pretty-elements/stepper.png)
  ; [stepper {...}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (stepper.prototypes/props-prototype                   id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
