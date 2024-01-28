
(ns pretty-inputs.checkbox.views
    (:require [fruits.hiccup.api                 :as hiccup]
              [fruits.random.api                 :as random]
              [fruits.vector.api                 :as vector]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.api               :as pretty-elements]
              [pretty-engine.api                 :as pretty-engine]
              [pretty-forms.api                  :as pretty-forms]
              [pretty-inputs.checkbox.attributes :as checkbox.attributes]
              [pretty-inputs.checkbox.prototypes :as checkbox.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-option
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:option-helper-f (function)
  ;  :option-label-f (function)}
  ; @param (*) option
  [checkbox-id {:keys [option-helper-f option-label-f] :as checkbox-props} option]
  [:button (checkbox.attributes/checkbox-option-attributes checkbox-id checkbox-props option)
           [:div (checkbox.attributes/checkbox-option-button-attributes checkbox-id checkbox-props option)
                 (if (pretty-engine/input-option-selected? checkbox-id checkbox-props option)
                     [:div (checkbox.attributes/checkbox-option-checkmark-attributes checkbox-id checkbox-props option) :done])]
           [:div {:class :pi-checkbox--option-content}
                 (if-some [option-label (-> option option-label-f)]
                          [:div (checkbox.attributes/checkbox-option-label-attributes checkbox-id checkbox-props option)
                                [metamorphic-content/compose option-label]])
                 (if-some [option-helper (-> option option-helper-f)]
                          [:div (checkbox.attributes/checkbox-option-helper-attributes checkbox-id checkbox-props option)
                                [metamorphic-content/compose option-helper]])]])

(defn- checkbox-option-list
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:placeholder (metamorphic-content)(opt)}
  [checkbox-id {:keys [placeholder] :as checkbox-props}]
  (letfn [(f0 [option] [checkbox-option checkbox-id checkbox-props option])]
         (let [options (pretty-engine/get-input-options checkbox-id checkbox-props)]
              (cond (-> options vector/not-empty?) (hiccup/put-with [:<>] options f0)
                    (-> placeholder) [:div (checkbox.attributes/checkbox-placeholder-attributes checkbox-id checkbox-props)
                                           (metamorphic-content/compose placeholder)]))))

(defn- checkbox
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div (checkbox.attributes/checkbox-attributes checkbox-id checkbox-props)
        (if-let [label-props (pretty-engine/input-label-props checkbox-id checkbox-props)]
                [pretty-elements/label label-props])
        [pretty-forms/invalid-message     checkbox-id checkbox-props]
        [pretty-engine/input-synchronizer checkbox-id checkbox-props]
        [:div (checkbox.attributes/checkbox-body-attributes checkbox-id checkbox-props)
              [checkbox-option-list                         checkbox-id checkbox-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-lifecycles
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-engine/input-did-mount    checkbox-id checkbox-props))
                       :component-will-unmount (fn [_ _] (pretty-engine/input-will-unmount checkbox-id checkbox-props))
                       :reagent-render         (fn [_ checkbox-props] [checkbox checkbox-id checkbox-props])}))

(defn input
  ; @param (keyword)(opt) checkbox-id
  ; @param (map) checkbox-props
  ; {:border-color (keyword or string)(opt)
  ;   Default: :default
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;   Default: :opacity
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :get-options-f (function)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :max-selection (integer)(opt)
  ;  :on-changed-f (function)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-invalid-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-selected-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-unselected-f (function)(opt)
  ;  :on-valid-f (function)(opt)
  ;  :option-color-f (function)(opt)
  ;  :option-helper-f (function)(opt)
  ;  :option-label-f (function)(opt)
  ;  :option-value-f (function)(opt)
  ;  :orientation (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :projected-value (*)(opt)
  ;  :set-value-f (function)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;     :invalid-message (metamorphic-content)(opt)}]}
  ;
  ; @usage
  ; [checkbox {...}]
  ;
  ; @usage
  ; [checkbox :my-checkbox {...}]
  ([checkbox-props]
   [input (random/generate-keyword) checkbox-props])

  ([checkbox-id checkbox-props]
   ; @note (tutorials#parametering)
   (fn [_ checkbox-props]
       (let [checkbox-props (pretty-presets/apply-preset                  checkbox-props)
             checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
            [input-lifecycles checkbox-id checkbox-props]))))
