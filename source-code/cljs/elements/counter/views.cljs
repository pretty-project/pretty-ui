
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.counter.views
    (:require [elements.counter.helpers    :as counter.helpers]
              [elements.counter.prototypes :as counter.prototypes]
              [elements.label.views        :as label.views]
              [mid-fruits.random           :as random]
              [re-frame.api                :as r]
              [reagent.api                 :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter-reset-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:resetable? (boolean)(opt)}
  [counter-id {:keys [resetable?] :as counter-props}]
  (if resetable? [:button.e-counter--reset-button (counter.helpers/reset-button-attributes counter-id counter-props)]))

(defn- counter-increase-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:disabled? (boolean)(opt)}
  [counter-id counter-props]
  [:button.e-counter--increase-button (counter.helpers/increase-button-attributes counter-id counter-props)])

(defn- counter-decrease-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:disabled? (boolean)(opt)}r/
  [counter-id counter-props]
  [:button.e-counter--decrease-button (counter.helpers/decrease-button-attributes counter-id counter-props)])

(defn- counter-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:value-path (vector)}
  [_ {:keys [value-path]}]
  (let [value @(r/subscribe [:db/get-item value-path])]
       [:div.e-counter--value value]))

(defn- counter-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  [:div.e-counter--body (counter.helpers/counter-body-attributes counter-id counter-props)
                        [counter-decrease-button                 counter-id counter-props]
                        [counter-value                           counter-id counter-props]
                        [counter-increase-button                 counter-id counter-props]
                        [counter-reset-button                    counter-id counter-props]])

(defn- counter-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  [_ {:keys [helper info-text label required?]}]
  (if label [label.views/element {:content     label
                                  :helper      helper
                                  :info-text   info-text
                                  :line-height :block
                                  :required?   required?}]))

(defn- counter-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  [:div.e-counter (counter.helpers/counter-attributes counter-id counter-props)
                  [counter-label                      counter-id counter-props]
                  [counter-body                       counter-id counter-props]])

(defn- counter
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  (reagent/lifecycles {:component-did-mount (counter.helpers/counter-did-mount-f counter-id counter-props)
                       :reagent-render      (fn [_ counter-props] [counter-structure counter-id counter-props])}))

(defn element
  ; @param (keyword)(opt) counter-id
  ; @param (map) counter-props
  ;  {:border-color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
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
  ;   :initial-value (integer)(opt)
  ;    Default: 0
  ;   :label (metamorphic-content)(opt)
  ;   :max-value (integer)(opt)
  ;   :min-value (integer)(opt)
  ;   :resetable? (boolean)(opt)
  ;    Default: false
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [counter {...}]
  ;
  ; @usage
  ;  [counter :my-counter {...}]
  ([counter-props]
   [element (random/generate-keyword) counter-props])

  ([counter-id counter-props]
   (let [counter-props (counter.prototypes/counter-props-prototype counter-id counter-props)]
        [counter counter-id counter-props])))