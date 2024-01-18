
(ns pretty-inputs.password-field.views
    (:require [fruits.random.api                       :as random]
              [pretty-inputs.password-field.prototypes :as password-field.prototypes]
              [pretty-inputs.text-field.views          :as text-field.views]
              [re-frame.api                            :as r]
              [reagent.api                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- password-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-will-unmount (fn [_ _] (r/dispatch [:pretty-inputs.password-field/field-will-unmount field-id field-props]))
                       :reagent-render         (fn [_ field-props] [text-field.views/input field-id field-props])}))

(defn input
  ; @info
  ; XXX#0711 (source-code/cljs/pretty_inputs/text_field/views.cljs)
  ; The 'password-field' element is based on the 'text-field' element.
  ; For more information, check out the documentation of the 'text-field' element.
  ;
  ; @description
  ; The 'password-field' element writes its actual value into the Re-Frame state
  ; delayed after the user stopped typing or without a delay when the user
  ; leaves the field!
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
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (password-field.prototypes/field-props-prototype field-id field-props)]
            [password-field field-id field-props]))))
