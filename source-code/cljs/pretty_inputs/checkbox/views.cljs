
(ns pretty-inputs.checkbox.views
    (:require [fruits.hiccup.api                 :as hiccup]
              [fruits.random.api                 :as random]
              [fruits.vector.api                 :as vector]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-inputs.checkbox.attributes :as checkbox.attributes]
              [pretty-inputs.checkbox.prototypes :as checkbox.prototypes]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-inputs.header.views        :as header.views]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-option
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (integer) option-dex
  ; @param (*) option
  [checkbox-id checkbox-props option-dex option]
  [:button (checkbox.attributes/checkbox-option-attributes checkbox-id checkbox-props option-dex option)
           [:div (checkbox.attributes/checkbox-option-button-attributes checkbox-id checkbox-props option-dex option)
                 (if (pretty-inputs.engine/input-option-selected? checkbox-id checkbox-props option-dex option)
                     [:div (checkbox.attributes/checkbox-option-checkmark-attributes checkbox-id checkbox-props option-dex option) :done])]
           [:div {:class :pi-checkbox--option-content}
                 (if-some [option-label (pretty-inputs.engine/get-input-option-label checkbox-id checkbox-props option-dex option)]
                          [:div (checkbox.attributes/checkbox-option-label-attributes checkbox-id checkbox-props option-dex option)
                                (-> option-label)])
                 (if-some [option-helper (pretty-inputs.engine/get-input-option-helper checkbox-id checkbox-props option-dex option)]
                          [:div (checkbox.attributes/checkbox-option-helper-attributes checkbox-id checkbox-props option-dex option)
                                (-> option-helper)])]])

(defn- checkbox-option-list
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:placeholder (metamorphic-content)(opt)}
  [checkbox-id {:keys [placeholder] :as checkbox-props}]
  (letfn [(f0 [option-dex option] [checkbox-option checkbox-id checkbox-props option-dex option])]
         (let [options (pretty-inputs.engine/get-input-options checkbox-id checkbox-props)]
              (cond (-> options vector/not-empty?) (hiccup/put-with-indexed [:<>] options f0)
                    (-> placeholder) [:div (checkbox.attributes/checkbox-placeholder-attributes checkbox-id checkbox-props)
                                           (metamorphic-content/compose placeholder)]))))

(defn- checkbox
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div (checkbox.attributes/checkbox-attributes checkbox-id checkbox-props)
        [pretty-inputs.header.views/view         checkbox-id checkbox-props]
        [pretty-inputs.engine/input-synchronizer checkbox-id checkbox-props]
        [:div (checkbox.attributes/checkbox-body-attributes checkbox-id checkbox-props)
              [checkbox-option-list                         checkbox-id checkbox-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    checkbox-id checkbox-props))
                       :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount checkbox-id checkbox-props))
                       :reagent-render         (fn [_ checkbox-props] [checkbox checkbox-id checkbox-props])}))

(defn view
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
  ;  :info (metamorphic-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker (map)(opt)
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
  ;   [{:error-text (metamorphic-content)(opt)
  ;     :f (function)}]}
  ;
  ; @usage
  ; [checkbox {...}]
  ;
  ; @usage
  ; [checkbox :my-checkbox {...}]
  ([checkbox-props]
   [view (random/generate-keyword) checkbox-props])

  ([checkbox-id checkbox-props]
   ; @note (tutorials#parameterizing)
   (fn [_ checkbox-props]
       (let [checkbox-props (pretty-presets.engine/apply-preset           checkbox-id checkbox-props)
             checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
            [view-lifecycles checkbox-id checkbox-props]))))
