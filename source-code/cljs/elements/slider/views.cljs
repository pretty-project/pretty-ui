
; WARNING! HASN'T FINISHED! DO NOT USE!

(ns elements.slider.views
    (:require [elements.element.views     :as element.views]
              [elements.slider.attributes :as slider.attributes]
              [elements.slider.prototypes :as slider.prototypes]
              [random.api                 :as random]
              [re-frame.api               :as r]
              [reagent.api                :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- slider-structure
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div (slider.attributes/slider-attributes slider-id slider-props)
        [element.views/element-label         slider-id slider-props]
        [:div (slider.attributes/slider-body-attributes slider-id slider-props)
              [:div {:class :e-slider--track}]
              [:div (slider.attributes/slider-line-attributes            slider-id slider-props)]
              [:div (slider.attributes/slider-primary-thumb-attributes   slider-id slider-props)]
              [:div (slider.attributes/slider-secondary-thumb-attributes slider-id slider-props)]]])

(defn- slider
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.slider/slider-did-mount slider-id slider-props]))
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
