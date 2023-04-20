
(ns elements.radio-button.views
    (:require [elements.element.views           :as element.views]
              [elements.input.env               :as input.env]
              [elements.radio-button.attributes :as radio-button.attributes]
              [elements.radio-button.prototypes :as radio-button.prototypes]
              [hiccup.api                       :as hiccup]
              [metamorphic-content.api          :as metamorphic-content]
              [random.api                       :as random]
              [re-frame.api                     :as r]
              [reagent.api                      :as reagent]))

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
           [:div (radio-button.attributes/radio-button-option-button-attributes button-id button-props)]
           [:div {:class :e-radio-button--option-content :data-click-target :opacity}
                 (if option-label-f  [:div (radio-button.attributes/radio-button-option-label-attributes button-id button-props)
                                           (-> option option-label-f metamorphic-content/compose)])
                 (if option-helper-f [:div (radio-button.attributes/radio-button-option-helper-attributes button-id button-props)
                                           (-> option option-helper-f metamorphic-content/compose)])]])

(defn- radio-button-structure
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:deselectable? (boolean)(opt)}
  [button-id {:keys [deselectable?] :as button-props}]
  [:div (radio-button.attributes/radio-button-attributes button-id button-props)
        [element.views/element-label button-id button-props]
        (if deselectable? [:button (radio-button.attributes/clear-button-attributes button-id button-props)])
                                   ; [:div.e-radio-button--clear-button-label (metamorphic-content/compose :delete!)]
        [:div (radio-button.attributes/radio-button-body-attributes button-id button-props)
              (let [options (input.env/get-input-options button-id button-props)]
                   (letfn [(f [option] [radio-button-option button-id button-props option])]
                          (hiccup/put-with [:<>] options f)))]])

(defn- radio-button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.radio-button/button-did-mount button-id button-props]))
                       :reagent-render      (fn [_ button-props] [radio-button-structure button-id button-props])}))

(defn element
  ; @param (keyword) button-id
  ; @param (map) button-props
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
  ;  :default-value (*)(opt)
  ;  :deselectable? (boolean)(opt)
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
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :on-select (Re-Frame metamorphic-event)(opt)
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
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [radio-button {...}]
  ;
  ; @usage
  ; [radio-button :my-radio-button {...}]
  ;
  ; @usage
  ; [radio-button :my-radio-button {:options [{:value :foo :label "Foo"} {:value :bar :label "Bar"}]}]
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (radio-button.prototypes/button-props-prototype button-id button-props)]
        [radio-button button-id button-props])))
