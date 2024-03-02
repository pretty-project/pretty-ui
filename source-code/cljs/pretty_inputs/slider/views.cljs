
(ns pretty-inputs.slider.views
    (:require [fruits.random.api               :as random]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.slider.attributes :as slider.attributes]
              [pretty-inputs.slider.prototypes :as slider.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [re-frame.api                    :as r]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- slider
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  [:div (slider.attributes/slider-attributes slider-id slider-props)
        ;[pretty-inputs.engine/input-label slider-id slider-props]
        [:div (slider.attributes/slider-inner-attributes slider-id slider-props)
              [:div {:class :pi-slider--track}]
              [:div (slider.attributes/slider-line-attributes            slider-id slider-props)]
              [:div (slider.attributes/slider-primary-thumb-attributes   slider-id slider-props)]
              [:div (slider.attributes/slider-secondary-thumb-attributes slider-id slider-props)]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  [slider-id slider-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount (fn [_ _] (r/dispatch [:pretty-inputs.slider/slider-did-mount slider-id slider-props]))
                         :reagent-render      (fn [_ slider-props] [slider slider-id slider-props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (keyword)(opt) slider-id
  ; @param (map) slider-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :helper (multitype-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info (multitype-content)(opt)
  ;  :initial-value (vector)(opt)
  ;   Default: [0 100]
  ;  :label (multitype-content)(opt)
  ;  :max-value (integer)(opt)
  ;   Default: 100
  ;  :min-value (integer)(opt)
  ;   Default: 0
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :resetable? (boolean)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :value-path (Re-Frame path vector)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [slider {...}]
  ;
  ; @usage
  ; [slider :my-slider {...}]
  ([slider-props]
   [view (random/generate-keyword) slider-props])

  ([slider-id slider-props]
   ; @note (tutorials#parameterizing)
   (fn [_ slider-props]
       (let [slider-props (pretty-presets.engine/apply-preset       slider-id slider-props)
             slider-props (slider.prototypes/slider-props-prototype slider-id slider-props)]
            [view-lifecycles slider-id slider-props]))))
