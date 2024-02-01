
(ns pretty-inputs.date-field.views
    (:require [fruits.random.api                   :as random]
              [pretty-inputs.date-field.prototypes :as date-field.prototypes]
              [pretty-inputs.text-field.views      :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @note
  ; For more information, check out the documentation of the ['text-field'](#text-field) input.
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
   [view (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (date-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/view field-id field-props]))))
