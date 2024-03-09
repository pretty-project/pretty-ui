
(ns pretty-inputs.combo-box.views
    (:require [fruits.hiccup.api                  :as hiccup]
              [fruits.loop.api                    :refer [reduce-indexed]]
              [fruits.random.api                  :as random]
              [multitype-content.api              :as multitype-content]
              [pretty-inputs.combo-box.attributes :as combo-box.attributes]
              [pretty-inputs.combo-box.env        :as combo-box.env]
              [pretty-inputs.combo-box.prototypes :as combo-box.prototypes]
              [pretty-inputs.engine.api           :as pretty-inputs.engine]
              [pretty-inputs.field.views          :as field.views]
              [pretty-inputs.header.views         :as header.views]
              [pretty-inputs.option-group.views   :as option-group.views]
              [pretty-inputs.text-field.views     :as text-field.views]
              [pretty-presets.engine.api          :as pretty-presets.engine]
              [pretty-subitems.api                :as pretty-subitems]
              [re-frame.api                       :as r]
              [reagent.core                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- combo-box-option
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-component (Reagent component symbol)(opt)
  ;  :option-label-f (function)}
  ; @param (integer) option-dex
  ; @param (map) option
  [box-id {:keys [option-component option-label-f] :as box-props} option-dex option]
  ; If no 'option-component' is passed, it displays the option in a DIV with the default ':pi-combo-box--option' class.
  [:button (combo-box.attributes/combo-box-option-attributes box-id box-props option-dex option)
           (if option-component [option-component box-id box-props option]
                                [:div {:class :pi-combo-box--option-label}
                                      (-> option option-label-f multitype-content/compose)])])

(defn- combo-box-options
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [options (combo-box.env/get-rendered-options box-id box-props)]
       (letfn [(f0 [option-dex option] [combo-box-option box-id box-props option-dex option])]
              [:div (combo-box.attributes/combo-box-options-attributes box-id box-props)
                    (hiccup/put-with-indexed [:<>] options f0)])))

(defn- combo-box-surface-content
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div (combo-box.attributes/combo-box-surface-content-attributes box-id box-props)
        [combo-box-options box-id box-props]])

(defn- combo-box
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:field (map)(opt)
  ;  :header (map)(opt)
  ;  :option-group (map)(opt)
  ;  ...}
  [id {:keys [field header option-group] :as props}]
  (let [option-group [option-group.views/view (pretty-subitems/subitem-id id :option-group) option-group]
        field        (assoc-in field [:expandable :content] option-group)]
       [:div (combo-box.attributes/outer-attributes id props)
             [:div (combo-box.attributes/inner-attributes id props)
                   (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
                   (if field  [field.views/view  (pretty-subitems/subitem-id id :field)  field])]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/pseudo-input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/pseudo-input-will-unmount id props))
                         :reagent-render         (fn [_ props] [combo-box id props])}))

(defn view
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ; {:field-content-f (function)(opt)
  ;   Default: return
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :initial-options (vector)(opt)
  ;  :max-selection (integer)(opt)
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :option-component (Reagent component symbol)(opt)
  ;   Default: pretty-inputs.combo-box.views/default-option-component
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options (vector)(opt)
  ;  :options-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [combo-box {...}]
  ;
  ; @usage
  ; [combo-box :my-combo-box {...}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset   id props)
             props (combo-box.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
