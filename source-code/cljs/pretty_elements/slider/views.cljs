
(ns pretty-elements.slider.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.element.views     :as element.views]
              [pretty-elements.slider.attributes :as slider.attributes]
              [pretty-elements.slider.prototypes :as slider.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]))

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
              [:div {:class :pe-slider--track}]
              [:div (slider.attributes/slider-line-attributes            slider-id slider-props)]
              [:div (slider.attributes/slider-primary-thumb-attributes   slider-id slider-props)]
              [:div (slider.attributes/slider-secondary-thumb-attributes slider-id slider-props)]]])

(defn- slider
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-elements.slider/slider-did-mount slider-id slider-props]))
                       :reagent-render      (fn [_ slider-props] [slider-structure slider-id slider-props])}))

(defn element
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (keyword)(opt) slider-id
  ; @param (map) slider-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (vector)(opt)
  ;   Default: [0 100]
  ;  :label (metamorphic-content)(opt)
  ;  :max-value (integer)(opt)
  ;   Default: 100
  ;  :min-value (integer)(opt)
  ;   Default: 0
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :resetable? (boolean)(opt)
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [slider {...}]
  ;
  ; @usage
  ; [slider :my-slider {...}]
  ([slider-props]
   [element (random/generate-keyword) slider-props])

  ([slider-id slider-props]
   ; @note (tutorials#parametering)
   (fn [_ slider-props]
       (let [slider-props (pretty-presets/apply-preset                        slider-props)
             slider-props (slider.prototypes/slider-props-prototype slider-id slider-props)]
            [slider slider-id slider-props]))))
