
(ns pretty-inputs.checkbox.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-inputs.checkbox.attributes :as checkbox.attributes]
              [pretty-inputs.checkbox.prototypes :as checkbox.prototypes]
              [pretty-inputs.input.views       :as input.views]
              [pretty-inputs.input.env           :as input.env]
              [pretty-presets.api                  :as pretty-presets]
              [re-frame.api                        :as r]
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
  [checkbox-id checkbox-props]
  (letfn [(f0 [option] [checkbox-option checkbox-id checkbox-props option])]
         (let [options (input.env/get-input-options checkbox-id checkbox-props)]
              (hiccup/put-with [:<>] options f0))))

(defn- checkbox
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div (checkbox.attributes/checkbox-attributes checkbox-id checkbox-props)
        [input.views/input-label                 checkbox-id checkbox-props]
        [:div (checkbox.attributes/checkbox-body-attributes checkbox-id checkbox-props)
              [checkbox-option-list                         checkbox-id checkbox-props]]])

(defn- checkbox-lifecycles
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-inputs.checkbox/checkbox-did-mount checkbox-id checkbox-props]))
                       :reagent-render      (fn [_ checkbox-props] [checkbox checkbox-id checkbox-props])}))

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
  ;  :helper (metamorphic-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-options (vector)(opt)
  ;  :initial-value (*)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-change (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the checked/unchecked option's value as parameter.
  ;  :on-checked (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the checked option's value as parameter.
  ;  :on-unchecked (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the unchecked option's value as parameter.
  ;  :option-helper-f (function)(opt)
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options (vector)(opt)
  ;  :options-orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :vertical
  ;  :options-path (Re-Frame path vector)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :projected-value (*)(opt)
  ;  :style (map)(opt)
  ;  :value (*)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
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
