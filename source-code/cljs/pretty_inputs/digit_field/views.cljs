
(ns pretty-inputs.digit-field.views
    (:require [fruits.random.api                    :as random]
              [pretty-inputs.digit-field.attributes :as digit-field.attributes]
              [pretty-inputs.digit-field.prototypes :as digit-field.prototypes]
              [pretty-inputs.engine.api             :as pretty-inputs.engine]
              [pretty-inputs.methods.api            :as pretty-inputs.methods]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- digit-field
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (digit-field.attributes/outer-attributes id props)
        [:div (digit-field.attributes/inner-attributes id props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount id props))
                         :reagent-render         (fn [_ props] [digit-field id props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ;
  ; @usage (pretty-inputs/digit-field.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props])))
