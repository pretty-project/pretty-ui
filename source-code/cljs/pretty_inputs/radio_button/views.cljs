
(ns pretty-inputs.radio-button.views
    (:require [fruits.hiccup.api                     :as hiccup]
              [fruits.random.api                     :as random]
              [fruits.vector.api                     :as vector]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-inputs.engine.api              :as pretty-inputs.engine]
              [pretty-inputs.header.views            :as header.views]
              [pretty-inputs.radio-button.attributes :as radio-button.attributes]
              [pretty-inputs.radio-button.prototypes :as radio-button.prototypes]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [reagent.api                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- radio-button-option
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (integer) option-dex
  ; @param (*) option
  [button-id button-props option-dex option]
  [:button (radio-button.attributes/radio-button-option-attributes button-id button-props option-dex option)
           [:div (radio-button.attributes/radio-button-option-button-attributes button-id button-props option-dex option)
                 (if (pretty-inputs.engine/input-option-selected? button-id button-props option-dex option)
                     [:div (radio-button.attributes/radio-button-option-thumb-attributes button-id button-props option-dex option)])]
           [:div {:class :pi-radio-button--option-content}
                 (if-some [option-label (pretty-inputs.engine/get-input-option-label button-id button-props option-dex option)]
                          [:div (radio-button.attributes/radio-button-option-label-attributes button-id button-props option-dex option)
                                (-> option-label)])
                 (if-some [option-helper (pretty-inputs.engine/get-input-option-helper button-id button-props option-dex option)]
                          [:div (radio-button.attributes/radio-button-option-helper-attributes button-id button-props option-dex option)
                                (-> option-helper)])]])

(defn- radio-button-option-list
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:placeholder (metamorphic-content)(opt)}
  [button-id {:keys [placeholder] :as button-props}]
  (letfn [(f0 [option-dex option] [radio-button-option button-id button-props option-dex option])]
         (let [options (pretty-inputs.engine/get-input-options button-id button-props)]
              (cond (-> options vector/not-empty?) (hiccup/put-with-indexed [:<>] options f0)
                    (-> placeholder) [:div (radio-button.attributes/radio-button-placeholder-attributes button-id button-props)
                                           (metamorphic-content/compose placeholder)]))))

(defn- radio-button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div (radio-button.attributes/radio-button-attributes button-id button-props)
        [pretty-inputs.header.views/view                 button-id button-props]
        [pretty-inputs.engine/input-synchronizer         button-id button-props]
        [:div (radio-button.attributes/radio-button-body-attributes button-id button-props)
              [radio-button-option-list                             button-id button-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    button-id button-props))
                       :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount button-id button-props))
                       :reagent-render         (fn [_ button-props] [radio-button button-id button-props])}))

(defn view
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
  ;  :info (metamorphic-content)(opt)
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)
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
  ; [radio-button {...}]
  ;
  ; @usage
  ; [radio-button :my-radio-button {...}]
  ([button-props]
   [view (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parameterizing)
   (fn [_ button-props]
       (let [button-props (pretty-presets.engine/apply-preset             button-id button-props)
             button-props (radio-button.prototypes/button-props-prototype button-id button-props)]
            [view-lifecycles button-id button-props]))))
