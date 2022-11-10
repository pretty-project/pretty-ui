
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.radio-button.views
    (:require [elements.input.helpers           :as input.helpers]
              [elements.label.views             :as label.views]
              [elements.radio-button.helpers    :as radio-button.helpers]
              [elements.radio-button.prototypes :as radio-button.prototypes]
              [mid-fruits.random                :as random]
              [reagent.api                      :as reagent]
              [x.app-components.api             :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- radio-button-option-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:option-helper-f (function)}
  ; @param (*) option
  [_ {:keys [option-helper-f]} option]
  (if option-helper-f (let [option-helper (option-helper-f option)]
                           [:div.e-radio-button--option-helper (x.components/content option-helper)])))

(defn- radio-button-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:option-label-f (function)}
  ; @param (*) option
  [_ {:keys [option-label-f]} option]
  (let [option-label (option-label-f option)]
       [:div.e-radio-button--option-label (x.components/content option-label)]))

(defn- radio-button-option-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  [button-id button-props option]
  [:div.e-radio-button--option-content [radio-button-option-label  button-id button-props option]
                                       [radio-button-option-helper button-id button-props option]])

(defn- radio-button-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  [button-id button-props option]
  [:button.e-radio-button--option (radio-button.helpers/radio-button-option-attributes button-id button-props option)
                                  [:div.e-radio-button--option-button]
                                  [radio-button-option-content button-id button-props option]])

(defn- radio-button-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (let [options (input.helpers/get-input-options button-id button-props)]
       (letfn [(f [option-list option] (conj option-list [radio-button-option button-id button-props option]))]
              (reduce f [:<>] options))))

(defn- radio-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.e-radio-button--body (radio-button.helpers/radio-button-body-attributes button-id button-props)
                             [radio-button-options                              button-id button-props]])

(defn- radio-button-unselect-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:unselectable? (boolean)(opt)}
  [button-id {:keys [unselectable?] :as button-props}]
  (if unselectable? [:button.e-radio-button--clear-button (radio-button.helpers/clear-button-attributes button-id button-props)]))
                                                         ;[:div.e-radio-button--clear-button-label (x.components/content :delete!)]

(defn- radio-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {}
  [_ {:keys [helper info-text label required?]}]
  (if label [label.views/element {:content     label
                                  :helper      helper
                                  :info-text   info-text
                                  :line-height :block
                                  :required?   required?}]))

(defn- radio-button-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.e-radio-button (radio-button.helpers/radio-button-attributes button-id button-props)
                       [radio-button-label                           button-id button-props]
                       [radio-button-unselect-button                 button-id button-props]
                       [radio-button-body                            button-id button-props]])

(defn- radio-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount (radio-button.helpers/radio-button-did-mount-f button-id button-props)
                       :reagent-render      (fn [_ button-props] [radio-button-structure button-id button-props])}))

(defn element
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:border-color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (*)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xs, :s, :inherit
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :info-text (metamorphic-content)(opt)
  ;   :initial-options (vector)(opt)
  ;   :initial-value (*)(opt)
  ;   :label (metamorphic-content)
  ;   :on-select (metamorphic-event)(opt)
  ;   :option-helper-f (function)(opt)
  ;   :option-label-f (function)(opt)
  ;    Default: return
  ;   :option-value-f (function)(opt)
  ;    Default: return
  ;   :options (vector)(opt)
  ;   :options-orientation (keyword)(opt)
  ;    :horizontal, :vertical
  ;    Default: :vertical
  ;   :options-path (vector)(opt)
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
  ;   :unselectable? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [radio-button {...}]
  ;
  ; @usage
  ;  [radio-button :my-radio-button {...}]
  ;
  ; @usage
  ;  [radio-button :my-radio-button {:options [{:value :foo :label "Foo"} {:value :bar :label "Bar"}]}]
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (radio-button.prototypes/button-props-prototype button-id button-props)]
        [radio-button button-id button-props])))