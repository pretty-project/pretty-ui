
(ns pretty-elements.checkbox.views
    (:require [pretty-elements.checkbox.attributes :as checkbox.attributes]
              [pretty-elements.checkbox.prototypes :as checkbox.prototypes]
              [pretty-elements.element.views       :as element.views]
              [pretty-elements.input.env           :as input.env]
              [hiccup.api                   :as hiccup]
              [metamorphic-content.api      :as metamorphic-content]
              [pretty-presets.api           :as pretty-presets]
              [random.api                   :as random]
              [re-frame.api                 :as r]
              [reagent.api                  :as reagent]))

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
           [:div {:class :pe-checkbox--option-content :data-click-target :opacity}
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

(defn- checkbox-structure
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div (checkbox.attributes/checkbox-attributes checkbox-id checkbox-props)
        [element.views/element-label             checkbox-id checkbox-props]
        [:div (checkbox.attributes/checkbox-body-attributes checkbox-id checkbox-props)
              [checkbox-option-list                         checkbox-id checkbox-props]]])

(defn- checkbox
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  ; XXX#0106 (README.md#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-elements.checkbox/checkbox-did-mount checkbox-id checkbox-props]))
                       :reagent-render      (fn [_ checkbox-props] [checkbox-structure checkbox-id checkbox-props])}))

(defn element
  ; @param (keyword)(opt) checkbox-id
  ; @param (map) checkbox-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;   Default: {:all :xs}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :default-value (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :initial-options (vector)(opt)
  ;  :initial-value (boolean)(opt)
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :on-check (Re-Frame metamorphic-event)(opt)
  ;  :on-uncheck (Re-Frame metamorphic-event)(opt)
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
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [checkbox {...}]
  ;
  ; @usage
  ; [checkbox :my-checkbox {...}]
  ([checkbox-props]
   [element (random/generate-keyword) checkbox-props])

  ([checkbox-id checkbox-props]
   (fn [_ checkbox-props] ; XXX#0106 (README.md#parametering)
       (let [checkbox-props (pretty-presets/apply-preset                              checkbox-props)
             checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
            [checkbox checkbox-id checkbox-props]))))
