
(ns elements.multi-field.views
    (:require [elements.combo-box.views        :as combo-box.views]
              [elements.multi-field.helpers    :as multi-field.helpers]
              [elements.multi-field.prototypes :as multi-field.prototypes]
              [elements.text-field.views       :as text-field.views]
              [loop.api                        :refer [reduce-indexed]]
              [random.api                      :as random]
              [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-field-text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  [group-id {:keys [initial-options options options-path] :as group-props} field-dex]
  (let [field-key   (multi-field.helpers/field-dex->react-key     group-id group-props field-dex)
        field-id    (multi-field.helpers/field-dex->field-id      group-id group-props field-dex)
        field-props (multi-field.prototypes/field-props-prototype group-id group-props field-dex)]
       [:div.e-multi-field--text-field {:key field-key}
                                       (if (or initial-options options options-path)
                                           [combo-box.views/element  field-id field-props]
                                           [text-field.views/element field-id field-props])]))

(defn- multi-field-field-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  (let [group-value @(r/subscribe [:elements.multi-field/get-group-value group-id group-props])]
       (letfn [(f [field-group field-dex _] (conj field-group [multi-field-text-field group-id group-props field-dex]))]
              (reduce-indexed f [:<>] group-value))))

(defn- multi-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div.e-multi-field (multi-field.helpers/group-attributes group-id group-props)
                      [multi-field-field-group              group-id group-props]])

(defn element
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; The multi-field element is based on the text-field element or the combo-box element.
  ; Check out the documentation of the text-field or the combo-box element for more information.
  ;
  ; In case of using the :initial-options, :options or :options-path properties,
  ; the multi-field element implements the combo-box element, otherwise it implements
  ; the text-field element.
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
   (let [group-props (multi-field.prototypes/group-props-prototype group-id group-props)]
        [multi-field group-id group-props])))
