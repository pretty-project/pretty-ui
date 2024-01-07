
(ns pretty-inputs.number-field.views
    (:require [fruits.random.api                       :as random]
              [pretty-inputs.number-field.prototypes :as number-field.prototypes]
              [pretty-inputs.text-field.views        :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input
  ; @info
  ; XXX#0711 (source-code/cljs/pretty_inputs/text_field/views.cljs)
  ; The 'number-field' element is based on the 'text-field' element.
  ; For more information, check out the documentation of the 'text-field' element.
  ;
  ; @description
  ; The 'number-field' element writes its actual value into the Re-Frame state
  ; delayed after the user stopped typing or without a delay when the user
  ; leaves the field!
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;
  ; @usage
  ; [number-field {...}]
  ;
  ; @usage
  ; [number-field :my-number-field {...}]
  ([field-props]
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (number-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/input field-id field-props]))))
