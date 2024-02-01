
(ns pretty-inputs.password-field.views
    (:require [fruits.random.api                         :as random]
              [pretty-inputs.password-field.prototypes   :as password-field.prototypes]
              [pretty-inputs.password-field.side-effects :as password-field.side-effects]
              [pretty-inputs.text-field.views            :as text-field.views]
              [reagent.api                               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- password-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [text-field.views/view field-id field-props])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-will-unmount (fn [_ _] (password-field.side-effects/field-will-unmount field-id field-props))
                       :reagent-render         (fn [_ field-props] [password-field field-id field-props])}))

(defn view
  ; @note
  ; For more information, check out the documentation of the ['text-field'](#text-field) input.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;
  ; @usage
  ; [password-field {...}]
  ;
  ; @usage
  ; [password-field :my-password-field {...}]
  ([field-props]
   [view (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (password-field.prototypes/field-props-prototype field-id field-props)]
            [view-lifecycles field-id field-props]))))
