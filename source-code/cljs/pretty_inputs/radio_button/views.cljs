
(ns pretty-inputs.radio-button.views
    (:require [fruits.hiccup.api                     :as hiccup]
              [fruits.random.api                     :as random]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-inputs.input.env               :as input.env]
              [pretty-inputs.radio-button.attributes :as radio-button.attributes]
              [pretty-inputs.radio-button.prototypes :as radio-button.prototypes]
              [pretty-inputs.core.env            :as core.env]
              [pretty-inputs.core.side-effects   :as core.side-effects]
              [pretty-inputs.core.views          :as core.views]
              [pretty-presets.api :as pretty-presets]
              [pretty-forms.api :as pretty-forms]
              [reagent.api                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- radio-button-option
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:option-helper-f (function)
  ;  :option-label-f (function)}
  ; @param (*) option
  [button-id {:keys [option-helper-f option-label-f] :as button-props} option]
  [:button (radio-button.attributes/radio-button-option-attributes button-id button-props option)
           [:div (radio-button.attributes/radio-button-option-button-attributes button-id button-props option)]
           [:div {:class :pi-radio-button--option-content}
                 (if-some [option-label (-> option option-label-f)]
                          [:div (radio-button.attributes/radio-button-option-label-attributes button-id button-props option)
                                [metamorphic-content/compose option-label]])
                 (if-some [option-helper (-> option option-helper-f)]
                          [:div (radio-button.attributes/radio-button-option-helper-attributes button-id button-props option)
                                [metamorphic-content/compose option-helper]])]])

(defn- radio-button-option-list
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (letfn [(f0 [option] [radio-button-option button-id button-props option])]
         (let [options (core.env/get-input-options button-id button-props)]
              (hiccup/put-with [:<>] options f0))))

(defn- radio-button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div (radio-button.attributes/radio-button-attributes button-id button-props)
        [core.views/input-synchronizer                   button-id button-props]
        [core.views/input-label                          button-id button-props]
        [pretty-forms/invalid-message                    button-id button-props]
        [:div (radio-button.attributes/radio-button-body-attributes button-id button-props)
              [radio-button-option-list                             button-id button-props]]])

(defn- radio-button-lifecycles
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    button-id button-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount button-id button-props))
                       :reagent-render         (fn [_ button-props] [radio-button button-id button-props])}))

(defn input
  ; @param (keyword) button-id
  ; @param (map) button-props
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
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-invalid-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-selected-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-unselected-f (function)(opt)
  ;  :on-valid-f (function)(opt)
  ;  :option-helper-f (function)(opt)
  ;  :option-label-f (function)(opt)
  ;  :option-value-f (function)(opt)
  ;  :orientation (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
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
  ; [radio-button {...}]
  ;
  ; @usage
  ; [radio-button :my-radio-button {...}]
  ;
  ; @usage
  ; [radio-button :my-radio-button {:initial-value "Option #1"
  ;                                 :get-options-f #(-> ["Option #1" "Option #2"])
  ;                                 :get-value-f   #(deref  my-atom)
  ;                                 :set-value-f   #(reset! my-atom %)}]
  ([button-props]
   [input (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parametering)
   (fn [_ button-props]
       (let [button-props (pretty-presets/apply-preset                              button-props)
             button-props (radio-button.prototypes/button-props-prototype button-id button-props)]
            [radio-button-lifecycles button-id button-props]))))
