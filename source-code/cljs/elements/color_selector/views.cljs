
(ns elements.color-selector.views
    (:require [elements.button.views              :as button.views]
              [elements.color-selector.attributes :as color-selector.attributes]
              [elements.color-selector.prototypes :as color-selector.prototypes]
              [elements.icon-button.views         :as icon-button.views]
              [layouts.api                        :as layouts]
              [random.api                         :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (string) option
  [selector-id selector-props option]
  [:button (color-selector.attributes/color-selector-option-attributes selector-id selector-props option)
           [:div {:class :e-color-selector--option--color
                  :style {:background-color option}}]])

(defn- color-selector-option-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:options (vector)}
  [selector-id {:keys [options] :as selector-props}]
  (letfn [(f [option-list option] (conj option-list [color-selector-option selector-id selector-props option]))]
         (reduce f [:<>] options)))

(defn color-selector-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  (let [on-close     [:x.ui/remove-popup! :elements.color-selector/options]
        close-button {:icon :expand_more :on-click on-close}]
       [:div {:class :e-color-selector--options}
             [layouts/popup-a :elements.color-selector/options
                              {:body [:div (color-selector.attributes/color-selector-body-attributes selector-id selector-props)
                                           [color-selector-option-list                               selector-id selector-props]]
                               :header [:div {:class :e-color-selector--options-header}
                                             [icon-button.views/element ::close-button close-button]]
                               :border-radius :m}]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The color-selector element is based on the button element.
  ; For more information check out the documentation of the button element.
  ;
  ; @description
  ; To render the color-selector popup without using its button element:
  ; [:elements.color-selector/render-selector! :my-color-selector {...}]
  ;
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :on-select (metamorphic-event)(opt)
  ;  :options (strings in vector)(opt)
  ;  :options-label (metamorphic-content)(opt)
  ;  :options-path (vector)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [color-selector {...}]
  ;
  ; @usage
  ; [color-selector :my-color-selector {...}]
  ([selector-props]
   [element (random/generate-keyword) selector-props])

  ([selector-id selector-props]
   (let [button-props (color-selector.prototypes/button-props-prototype selector-id selector-props)]
        [button.views/element selector-id button-props])))
