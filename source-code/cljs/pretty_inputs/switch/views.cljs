
(ns pretty-inputs.switch.views
    (:require [fruits.hiccup.api               :as hiccup]
              [fruits.random.api               :as random]
              [metamorphic-content.api         :as metamorphic-content]
              [pretty-build-kit.api            :as pretty-build-kit]
              [pretty-inputs.input.env         :as input.env]
              [pretty-inputs.switch.attributes :as switch.attributes]
              [pretty-inputs.switch.prototypes :as switch.prototypes]
              [pretty-presets.api              :as pretty-presets]
              [pretty-forms.api :as pretty-forms]
              [pretty-inputs.core.env            :as core.env]
              [pretty-inputs.core.side-effects   :as core.side-effects]
              [pretty-inputs.core.views          :as core.views]
              [reagent.api                     :as reagent]
              [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-option
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:option-helper-f (function)
  ;  :option-label-f (function)}
  ; @param (*) option
  [switch-id {:keys [option-helper-f option-label-f] :as switch-props} option]
  [:button (switch.attributes/switch-option-attributes switch-id switch-props option)
           [:div (switch.attributes/switch-option-track-attributes switch-id switch-props option)
                 [:div (switch.attributes/switch-option-thumb-attributes switch-id switch-props option)]]
           [:div {:class :pi-switch--option-content}
                 (if-some [option-label (-> option option-label-f)]
                          [:div (switch.attributes/switch-option-label-attributes switch-id switch-props option)
                                [metamorphic-content/compose option-label]])
                 (if-some [option-helper (-> option option-helper-f)]
                          [:div (switch.attributes/switch-option-helper-attributes switch-id switch-props option)
                                [metamorphic-content/compose option-helper]])]])

(defn- switch-option-list
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:placeholder (metamorphic-content)(opt)}
  [switch-id {:keys [placeholder] :as switch-props}]
  (letfn [(f0 [option] [switch-option switch-id switch-props option])]
         (let [options (core.env/get-input-options switch-id switch-props)]
              (cond (-> options vector/not-empty?) (hiccup/put-with [:<>] options f0)
                    (-> placeholder) [:div (switch.attributes/switch-placeholder-attributes switch-id switch-props)
                                           (metamorphic-content/compose placeholder)]))))

(defn- switch
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:div (switch.attributes/switch-attributes switch-id switch-props)
        [core.views/input-synchronizer       switch-id switch-props]
        [core.views/input-label              switch-id switch-props]
        [pretty-forms/invalid-message        switch-id switch-props]
        [:div (switch.attributes/switch-body-attributes switch-id switch-props)
              [switch-option-list                       switch-id switch-props]]])

(defn- switch-lifecycles
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    switch-id switch-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount switch-id switch-props))
                       :reagent-render         (fn [_ switch-props] [switch switch-id switch-props])}))

(defn input
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
  ;  :get-options-f (function)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-options (vector)(opt)
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
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;     :invalid-message (metamorphic-content)(opt)}]}
  ;
  ; @usage
  ; [switch {...}]
  ;
  ; @usage
  ; [switch :my-switch {...}]
  ([switch-props]
   [input (random/generate-keyword) switch-props])

  ([switch-id switch-props]
   ; @note (tutorials#parametering)
   (fn [_ switch-props]
       (let [switch-props (pretty-presets/apply-preset                        switch-props)
             switch-props (switch.prototypes/switch-props-prototype switch-id switch-props)]
            [switch-lifecycles switch-id switch-props]))))
