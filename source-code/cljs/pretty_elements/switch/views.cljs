
(ns pretty-elements.switch.views
    (:require [fruits.hiccup.api                 :as hiccup]
              [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-css.api                    :as pretty-css]
              [pretty-elements.element.views     :as element.views]
              [pretty-elements.input.env         :as input.env]
              [pretty-elements.switch.attributes :as switch.attributes]
              [pretty-elements.switch.prototypes :as switch.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-option
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:option-helper-f (function)(opt)
  ;  :option-label-f (function)}
  ; @param (*) option
  [switch-id {:keys [option-helper-f option-label-f] :as switch-props} option]
  [:button (switch.attributes/switch-option-attributes switch-id switch-props option)
           [:div (switch.attributes/switch-option-track-attributes switch-id switch-props)]
           [:div {:class :pe-switch--option-content :data-click-target :opacity}
                 (if option-label-f  [:div (switch.attributes/switch-option-label-attributes switch-id switch-props)
                                           (-> option option-label-f metamorphic-content/compose)])
                 (if option-helper-f [:div (switch.attributes/switch-option-helper-attributes switch-id switch-props)
                                           (-> option option-helper-f metamorphic-content/compose)])]])

(defn- switch-options
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  (let [options (input.env/get-input-options switch-id switch-props)]
       (letfn [(f0 [option] [switch-option switch-id switch-props option])]
              (hiccup/put-with [:<>] options f0))))

(defn- switch-structure
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:div (switch.attributes/switch-attributes switch-id switch-props)
        [element.views/element-label         switch-id switch-props]
        [:div (switch.attributes/switch-body-attributes switch-id switch-props)
              [switch-options                           switch-id switch-props]]])

(defn- switch
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-elements.switch/switch-did-mount switch-id switch-props]))
                       :reagent-render      (fn [_ switch-props] [switch-structure switch-id switch-props])}))

(defn element
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
  ; {:border-color (keyword or string)(opt)
  ;   Default: :default
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;   Default: {:all :m}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;   :none, :opacity
  ;   Default: :opacity
  ;  :default-value (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;   :none, :opacity
  ;   Default: :none
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-options (vector)(opt)
  ;  :initial-value (boolean)(opt)
  ;  :label (metamorphic-content)(opt)
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
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [switch {...}]
  ;
  ; @usage
  ; [switch :my-switch {...}]
  ([switch-props]
   [element (random/generate-keyword) switch-props])

  ([switch-id switch-props]
   ; @note (tutorials#parametering)
   (fn [_ switch-props]
       (let [switch-props (pretty-presets/apply-preset                        switch-props)
             switch-props (switch.prototypes/switch-props-prototype switch-id switch-props)]
            [switch switch-id switch-props]))))
