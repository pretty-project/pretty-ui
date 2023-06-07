
(ns elements.switch.views
    (:require [elements.element.views     :as element.views]
              [elements.input.env         :as input.env]
              [elements.switch.attributes :as switch.attributes]
              [elements.switch.prototypes :as switch.prototypes]
              [hiccup.api                 :as hiccup]
              [metamorphic-content.api    :as metamorphic-content]
              [pretty-css.api             :as pretty-css]
              [random.api                 :as random]
              [re-frame.api               :as r]
              [reagent.api                :as reagent]))

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
           [:div {:class :e-switch--option-content :data-click-target :opacity}
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
       (letfn [(f [option] [switch-option switch-id switch-props option])]
              (hiccup/put-with [:<>] options f))))

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
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.switch/switch-did-mount switch-id switch-props]))
                       :reagent-render      (fn [_ switch-props] [switch-structure switch-id switch-props])}))

(defn element
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
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
  ;   Default: {:all :m}
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
   (let [switch-props (switch.prototypes/switch-props-prototype switch-id switch-props)]
        [switch switch-id switch-props])))
