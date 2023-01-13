
(ns elements.color-selector.views
    (:require [elements.button.views              :as button.views]
              [elements.color-selector.helpers    :as color-selector.helpers]
              [elements.color-selector.prototypes :as color-selector.prototypes]
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
  [:button.e-color-selector--option (color-selector.helpers/color-selector-option-attributes selector-id selector-props option)
                                    [:div.e-color-selector--option--color {:style {:background-color option}}]])

(defn- color-selector-option-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:options (vector)}
  [selector-id {:keys [options] :as selector-props}]
  (letfn [(f [option-list option] (conj option-list [color-selector-option selector-id selector-props option]))]
         (reduce f [:<>] options)))

(defn- color-selector-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:div.e-color-selector--body (color-selector.helpers/color-selector-body-attributes selector-id selector-props)
                               [color-selector-option-list                            selector-id selector-props]])

(defn- color-selector-options-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [layouts/popup-a :elements.color-selector/options
                   {:body [color-selector-body selector-id selector-props]}])

(defn color-selector-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:div.e-color-selector--options [color-selector-options-structure selector-id selector-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The color-selector element is based on the button element.
  ; For more information check out the documentation of the button element.
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
