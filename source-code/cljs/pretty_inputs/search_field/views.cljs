
(ns pretty-inputs.search-field.views
    (:require [fruits.random.api                     :as random]
              [pretty-inputs.search-field.prototypes :as search-field.prototypes]
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
  ; [search-field {...}]
  ;
  ; @usage
  ; [search-field :my-search-field {...}]
  ([field-props]
   [view (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (search-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/view field-id field-props]))))
