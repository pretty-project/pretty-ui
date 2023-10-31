
(ns elements.multiline-field.views
    (:require [elements.multiline-field.prototypes :as multiline-field.prototypes]
              [elements.text-field.views           :as text-field.views]
              [random.api                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @info
  ; XXX#0711 (source-code/cljs/elements/text_field/views.cljs)
  ; The 'multiline-field' element is based on the 'text-field' element.
  ; For more information check out the documentation of the 'text-field' element.
  ;
  ; @description
  ; The 'multiline-field' element writes its actual value into the Re-Frame state
  ; delayed, after the user stopped typing or without a delay when the user leaves the field!
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:max-height (integer)(opt)
  ;   TODO
  ;   Max line count
  ;   Default: 32
  ;  :min-height (integer)(opt)
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
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props] ; XXX#0106 (README.md#parametering)
       (let [field-props (multiline-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/element field-id field-props]))))
