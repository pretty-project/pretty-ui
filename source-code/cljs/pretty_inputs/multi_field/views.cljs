
(ns pretty-inputs.multi-field.views
    (:require [fruits.hiccup.api                    :as hiccup]
              [fruits.loop.api                      :refer [reduce-indexed]]
              [fruits.random.api                    :as random]
              [pretty-inputs.combo-box.views        :as combo-box.views]
              [pretty-inputs.multi-field.attributes :as multi-field.attributes]
              [pretty-inputs.multi-field.prototypes :as multi-field.prototypes]
              [pretty-inputs.multi-field.utils      :as multi-field.utils]
              [pretty-inputs.text-field.views       :as text-field.views]
              [re-frame.api                         :as r]
              [reagent.api :as reagent]))

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
       [:div {:class :pi-multi-field--text-field :key field-key}
             (if (or initial-options options options-path)
                 [combo-box.views/view  field-id field-props]
                 [text-field.views/view field-id field-props])]))

(defn- multi-field
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div (multi-field.attributes/group-attributes group-id group-props)
        (let [group-value @(r/subscribe [:pretty-inputs.multi-field/get-group-value group-id group-props])]
             (letfn [(f0 [field-dex _] [multi-field-text-field group-id group-props field-dex])]
                    (hiccup/put-with-indexed [:<>] group-value f0)))])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount (fn [_ _]); (r/dispatch [:pretty-inputs.group/group-did-mount group-id group-props]))
                       :reagent-render      (fn [_ group-props] [multi-field group-id group-props])}))

(defn view
  ; @note
  ; For more information, check out the documentation of the ['text-field'](#text-field) and ['combo-box'](#combo-box) inputs.
  ;
  ; @description
  ; In case of using the ':initial-options', ':options' or the ':options-path' properties, the 'multi-field'
  ; element implements the 'combo-box' element. Otherwise, it implements the 'text-field' element.
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
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (multi-field.prototypes/group-props-prototype group-id group-props)]
            [view-lifecycles group-id group-props]))))
