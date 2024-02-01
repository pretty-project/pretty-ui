
(ns pretty-inputs.number-field.views
    (:require [fruits.random.api                     :as random]
              [pretty-inputs.number-field.prototypes :as number-field.prototypes]
              [pretty-inputs.text-field.views        :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @note
  ; For more information, check out the documentation of the ['text-field'](#text-field) input.
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
   [view (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (number-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/view field-id field-props]))))
