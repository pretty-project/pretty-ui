
(ns elements.password-field.views
    (:require [elements.password-field.prototypes :as password-field.prototypes]
              [elements.text-field.views          :as text-field.views]
              [random.api                         :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; The password-field element is based on the text-field element.
  ; For more information check out the documentation of the text-field element.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:validate? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; [password-field {...}]
  ;
  ; @usage
  ; [password-field :my-password-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (password-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))
