
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.slider.views
    (:require [elements.slider.helpers    :as slider.helpers]
              [elements.slider.prototypes :as slider.prototypes]
              [elements.label.views       :as label.views]
              [random.api                 :as random]
              [reagent.api                :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- slider-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div.e-slider--line (slider.helpers/slider-line-attributes slider-id slider-props)])

(defn- slider-secondary-thumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div.e-slider--thumb (slider.helpers/slider-secondary-thumb-attributes slider-id slider-props)])

(defn- slider-primary-thumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div.e-slider--thumb (slider.helpers/slider-primary-thumb-attributes slider-id slider-props)])

(defn- slider-track
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div.e-slider--track])

(defn- slider-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div.e-slider--body [slider-track           slider-id slider-props]
                       [slider-line            slider-id slider-props]
                       [slider-primary-thumb   slider-id slider-props]
                       [slider-secondary-thumb slider-id slider-props]])

(defn- slider-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;  {}
  [_ {:keys [helper info-text label marked? required?]}]
  (if label [label.views/element {:content     label
                                  :helper      helper
                                  :info-text   info-text
                                  :line-height :block
                                  :marked?     marked?
                                  :required?   required?}]))

(defn- slider-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div.e-slider (slider.helpers/slider-attributes slider-id slider-props)
                 [slider-label                     slider-id slider-props]
                 [slider-body                      slider-id slider-props]])

(defn- slider
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (slider.helpers/slider-did-mount slider-id slider-props))
                       :reagent-render      (fn [_ slider-props] [slider-structure slider-id slider-props])}))

(defn element
  ; @param (keyword)(opt) slider-id
  ; @param (map) slider-props
  ;  {:class (keyword or keywords in vector)(opt)
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
  ;   :initial-value (vector)(opt)
  ;    Default: [0 100]
  ;   :label (metamorphic-content)(opt)
  ;   :marked? (boolean)(opt)
  ;    Default: false
  ;   :max-value (integer)(opt)
  ;    Default: 100
  ;   :min-value (integer)(opt)
  ;    Default: 0
  ;   :resetable? (boolean)(opt)
  ;    Default: false
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [slider {...}]
  ;
  ; @usage
  ;  [slider :my-slider {...}]
  ([slider-props]
   [element (random/generate-keyword) slider-props])

  ([slider-id slider-props]
   (let [slider-props (slider.prototypes/slider-props-prototype slider-id slider-props)]
        [slider slider-id slider-props])))
