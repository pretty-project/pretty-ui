
(ns elements.number-field.views
    (:require [elements.number-field.prototypes :as number-field.prototypes]
              [elements.text-field.views        :as text-field.views]
              [random.api                       :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0711 (source-code/cljs/elements/text_field/views.cljs)
  ; The number-field element is based on the text-field element.
  ; For more information check out the documentation of the text-field element.
  ;
  ; @description
  ; The number-field element writes its actual value into the Re-Frame state
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
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (number-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))
