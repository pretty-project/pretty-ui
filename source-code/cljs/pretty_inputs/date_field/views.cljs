
(ns pretty-inputs.date-field.views
    (:require [fruits.random.api                     :as random]
              [pretty-inputs.date-field.prototypes :as date-field.prototypes]
              [pretty-inputs.text-field.views      :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input
  ; @info
  ; XXX#0711 (source-code/cljs/pretty_inputs/text_field/views.cljs)
  ; The 'date-field' element is based on the 'text-field' element.
  ; For more information, check out the documentation of the 'text-field' element.
  ;
  ; @description
  ; The 'date-field' element writes its actual value into the Re-Frame state
  ; delayed after the user stopped typing or without a delay when the user
  ; leaves the field!
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:date-from (string)(opt)
  ;  :date-to (string)(opt)}
  ;
  ; @usage
  ; [date-field {...}]
  ;
  ; @usage
  ; [date-field :my-date-field {...}]
  ([field-props]
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (date-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/input field-id field-props]))))
