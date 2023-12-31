
(ns pretty-elements.switch.views
    (:require [fruits.hiccup.api                 :as hiccup]
              [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-build-kit.api                    :as pretty-build-kit]
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
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;   Default: {:all :m}
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
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-options (vector)(opt)
  ;  :initial-value (boolean)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword or string)(opt)
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
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :projected-value (*)(opt)
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
