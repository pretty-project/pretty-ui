
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.views
    (:require [mid-fruits.candy                              :refer [param]]
              [mid-fruits.vector                             :as vector]
              [x.app-components.api                          :as components]
              [x.app-core.api                                :as a]
              [x.app-elements.color-selector.prototypes      :as color-selector.prototypes]
              [x.app-elements.color-stamp.views              :refer [color-stamp]]
              [x.app-elements.engine.api                     :as engine]
              [x.app-environment.api                         :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) options-props
  ;  {:value-path (vector)}
  ; @param (string) option
  [selector-id {:keys [value-path] :as options-props} option]
  (let [on-click [:elements/toggle-color-selector-option! selector-id options-props option]
        selected-options @(a/subscribe [:db/get-item value-path])]
       [:button.x-color-selector--option {:data-clickable true
                                          :data-collected (vector/contains-item? selected-options option)
                                          :on-click      #(a/dispatch on-click)
                                          :on-mouse-up   #(environment/blur-element!)}
                                         [:div.x-color-selector--option--color {:style {:background-color option}}]]))

(defn color-selector-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) options-props
  [selector-id {:keys [options] :as options-props}]
  (reduce (fn [option-list option]
              (conj option-list [color-selector-option selector-id options-props option]))
          [:div.x-color-selector--options]
          (param options)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-colors-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:no-colors-label (metamorphic-content)(opt)}
  [selector-id {:keys [no-colors-label] :as selector-props}]
  [:div.x-color-selector--no-colors-label (components/content no-colors-label)])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:disabled? (boolean)(opt)
  ;   :size (keyword)
  ;   :value-path (vector)}
  [selector-id {:keys [disabled? size value-path] :as selector-props}]
  (let [on-click [:elements/render-color-selector-options! selector-id selector-props]
        colors   @(a/subscribe [:db/get-item value-path])]
       [:button.x-color-selector--body (if-not disabled? {:data-clickable true
                                                          :on-click       #(a/dispatch on-click)
                                                          :on-mouse-up    #(environment/blur-element!)})
                                       (if (vector/nonempty? colors)
                                           [color-stamp     selector-id {:colors colors :disabled? disabled? :size size}]
                                           [no-colors-label selector-id selector-props])]))

(defn- color-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:div.x-color-selector (engine/element-attributes selector-id selector-props)
                         [color-selector-body       selector-id selector-props]])

(defn element
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :no-colors-label (metamorphic-content)(opt)
  ;    Default: DEFAULT-NO-COLORS-LABEL
  ;   :options (strings in vector)(opt)
  ;   :options-path (vector)(opt)
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/color-selector {...}]
  ;
  ; @usage
  ;  [elements/color-selector :my-color-selector {...}]
  ;
  ; @usage
  ;  [elements/color-selector :my-color-selector {:options ["red" "green" "blue"]}]
  ([selector-props]
   [element (a/id) selector-props])

  ([selector-id selector-props]
   (let [selector-props (color-selector.prototypes/selector-props-prototype selector-id selector-props)]
        [color-selector selector-id selector-props])))
