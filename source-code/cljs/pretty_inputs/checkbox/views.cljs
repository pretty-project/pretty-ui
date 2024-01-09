
(ns pretty-inputs.checkbox.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-forms.api :as pretty-forms]
              [pretty-inputs.checkbox.attributes :as checkbox.attributes]
              [pretty-inputs.checkbox.prototypes :as checkbox.prototypes]
              [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [pretty-inputs.core.views       :as core.views]
              [pretty-presets.api                  :as pretty-presets]
              [reagent.api                         :as reagent]))

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
           [:div (checkbox.attributes/checkbox-option-button-attributes checkbox-id checkbox-props option)]
           [:div {:class :pi-checkbox--option-content}
                 [:div (checkbox.attributes/checkbox-option-label-attributes checkbox-id checkbox-props option)
                       (-> option option-label-f metamorphic-content/compose)]
                 (if option-helper-f [:div (checkbox.attributes/checkbox-option-helper-attributes checkbox-id checkbox-props option)
                                           (-> option option-helper-f metamorphic-content/compose)])]])

(defn- checkbox-option-list
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  [checkbox-id {:keys [get-options-f] :as checkbox-props}]
  (letfn [(f0 [option] [checkbox-option checkbox-id checkbox-props option])]
         (let [options (core.env/get-input-options checkbox-id checkbox-props)]
              (hiccup/put-with [:<>] options f0))))

(defn- checkbox
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div (checkbox.attributes/checkbox-attributes checkbox-id checkbox-props)
        [core.views/input-synchronizer checkbox-id checkbox-props]
        [core.views/input-label        checkbox-id checkbox-props]
        (if-let [invalid-message (pretty-forms/get-input-invalid-message checkbox-id)]
                [:div {:class :pi-checkbox--invalid-message :data-selectable false}
                      (metamorphic-content/compose invalid-message)])
        [:div (checkbox.attributes/checkbox-body-attributes checkbox-id checkbox-props)
              [checkbox-option-list                         checkbox-id checkbox-props]]])

(defn- checkbox-lifecycles
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    checkbox-id checkbox-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount checkbox-id checkbox-props))
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
  ;   Must return the selectable options.
  ;  :get-value-f (function)(opt)
  ;   Must return the actual value.
  ;  :helper (metamorphic-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-value (*)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-invalid-f (function)(opt)
  ;   Takes the actual value and the invalid message as parameters.
  ;  :on-mounted-f (function)(opt)
  ;   Takes the actual value as parameter.
  ;  :on-selected-f (function)(opt)
  ;   Takes the actual value as parameter.
  ;  :on-unmounted-f (function)(opt)
  ;   Takes the actual value as parameter.
  ;  :on-unselected-f (function)(opt)
  ;   Takes the actual value as parameter.
  ;  :on-valid-f (function)(opt)
  ;   Takes the actual value as parameter.
  ;  :option-helper-f (function)(opt)
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options-orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :vertical
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :projected-value (*)(opt)
  ;  :set-value-f (function)(opt)
  ;   Takes the actual value as parameter.
  ;  :style (map)(opt)
  ;  :validate-when-change? (boolean)(opt)
  ;   Validates the value when it changes.
  ;  :validate-when-leave? (boolean)(opt)
  ;   Validates the value and turns on the autovalidation when the user leaves the input.
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;      Takes the actual value as parameter.
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
       (let [checkbox-props (pretty-presets/apply-preset                              checkbox-props)
             checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
            [checkbox-lifecycles checkbox-id checkbox-props]))))
