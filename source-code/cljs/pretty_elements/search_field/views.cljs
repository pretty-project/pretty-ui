
(ns pretty-elements.search-field.views
    (:require [fruits.random.api                       :as random]
              [pretty-elements.search-field.prototypes :as search-field.prototypes]
              [pretty-elements.text-field.views        :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @info
  ; XXX#0711 (source-code/cljs/pretty_elements/text_field/views.cljs)
  ; The 'search-field' element is based on the 'text-field' element.
  ; For more information, check out the documentation of the 'text-field' element.
  ;
  ; @description
  ; The 'search-field' element writes its actual value into the Re-Frame state
  ; delayed after the user stopped typing or without a delay when the user
  ; leaves the field!
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;
  ; @usage
  ; [search-field {...}]
  ;
  ; @usage
  ; [search-field :my-search-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props] ; XXX#0106 (README.md#parametering)
       (let [field-props (search-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/element field-id field-props]))))
