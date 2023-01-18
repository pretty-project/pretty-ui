
(ns elements.slider.views
    (:require [elements.element.views     :as element.views]
              [elements.slider.helpers    :as slider.helpers]
              [elements.slider.prototypes :as slider.prototypes]
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
  [:div.e-slider--body (slider.helpers/slider-body-attributes slider-id slider-props)
                       [slider-track                          slider-id slider-props]
                       [slider-line                           slider-id slider-props]
                       [slider-primary-thumb                  slider-id slider-props]
                       [slider-secondary-thumb                slider-id slider-props]])

(defn- slider-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div.e-slider (slider.helpers/slider-attributes slider-id slider-props)
                 [element.views/element-label      slider-id slider-props]
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
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
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
  ;  :initial-value (vector)(opt)
  ;   Default: [0 100]
  ;  :label (metamorphic-content)(opt)
  ;  :max-value (integer)(opt)
  ;   Default: 100
  ;  :min-value (integer)(opt)
  ;   Default: 0
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :resetable? (boolean)(opt)
  ;  :style (map)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [slider {...}]
  ;
  ; @usage
  ; [slider :my-slider {...}]
  ([slider-props]
   [element (random/generate-keyword) slider-props])

  ([slider-id slider-props]
   (let [slider-props (slider.prototypes/slider-props-prototype slider-id slider-props)]
        [slider slider-id slider-props])))
