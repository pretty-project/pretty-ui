
(ns elements.multi-field.views
    (:require [elements.combo-box.views        :as combo-box.views]
              [elements.multi-field.attributes :as multi-field.attributes]
              [elements.multi-field.prototypes :as multi-field.prototypes]
              [elements.multi-field.utils      :as multi-field.utils]
              [elements.text-field.views       :as text-field.views]
              [hiccup.api                      :as hiccup]
              [loop.api                        :refer [reduce-indexed]]
              [random.api                      :as random]
              [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-field-text-field
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  [group-id {:keys [initial-options options options-path] :as group-props} field-dex]
  (let [field-key   (multi-field.utils/field-dex->react-key       group-id group-props field-dex)
        field-id    (multi-field.utils/field-dex->field-id        group-id group-props field-dex)
        field-props (multi-field.prototypes/field-props-prototype group-id group-props field-dex)]
       [:div {:class :e-multi-field--text-field :key field-key}
             (if (or initial-options options options-path)
                 [combo-box.views/element  field-id field-props]
                 [text-field.views/element field-id field-props])]))

(defn- multi-field
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div (multi-field.attributes/group-attributes group-id group-props)
        (let [group-value @(r/subscribe [:elements.multi-field/get-group-value group-id group-props])]
             (letfn [(f [field-dex _] [multi-field-text-field group-id group-props field-dex])]
                    (hiccup/put-with-indexed [:<>] group-value f)))])

(defn element
  ; @info
  ; XXX#0711 (source-code/cljs/elements/text_field/views.cljs)
  ; The 'multi-field' element is based on the 'text-field' or the 'combo-box' element.
  ; For more information check out the documentation of the 'text-field' or the 'combo-box' element.
  ;
  ; @description
  ; - The 'multi-field' element writes its actual value into the Re-Frame state delayed, after
  ;   the user stopped typing or without a delay when the user leaves the field!
  ; - In case of using the ':initial-options', ':options' or the ':options-path' properties, the 'multi-field'
  ;   element implements the 'combo-box' element, otherwise it implements the 'text-field' element.
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:max-field-count (integer)(opt)
  ;   Default: 8}
  ;
  ; @usage
  ; [multi-field {...}]
  ;
  ; @usage
  ; [multi-field :my-multi-field {...}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (fn [_ group-props] ; XXX#0106 (README.md#parametering)
       (let [group-props (multi-field.prototypes/group-props-prototype group-id group-props)]
            [multi-field group-id group-props]))))
