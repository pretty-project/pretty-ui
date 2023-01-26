
(ns elements.date-field.views
    (:require [elements.date-field.prototypes :as date-field.prototypes]
              [elements.text-field.views      :as text-field.views]
              [random.api                     :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0711 (source-code/cljs/elements/text_field/views.cljs)
  ; The date-field element is based on the text-field element.
  ; For more information check out the documentation of the text-field element.
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
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (date-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))
