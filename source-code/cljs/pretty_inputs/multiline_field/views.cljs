
(ns pretty-inputs.multiline-field.views
    (:require [fruits.random.api                        :as random]
              [pretty-inputs.multiline-field.prototypes :as multiline-field.prototypes]
              [pretty-inputs.text-field.views           :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input
  ; @note
  ; For more information, check out the documentation of the ['text-field'](#text-field) input.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:max-lines (integer)(opt)
  ;   TODO
  ;   Max line count
  ;   Default: 32
  ;  :min-lines (integer)(opt)
  ;   TODO
  ;   Min line count
  ;   Default: 1}
  ;
  ; @usage
  ; [multiline-field {...}]
  ;
  ; @usage
  ; [multiline-field :my-multiline-field {...}]
  ([field-props]
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (multiline-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/input field-id field-props]))))
