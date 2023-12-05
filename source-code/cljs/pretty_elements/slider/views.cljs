
(ns pretty-elements.slider.views
    (:require [pretty-elements.element.views     :as element.views]
              [pretty-elements.slider.attributes :as slider.attributes]
              [pretty-elements.slider.prototypes :as slider.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [random.api                        :as random]
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
  ; XXX#0106 (README.md#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-elements.slider/slider-did-mount slider-id slider-props]))
                       :reagent-render      (fn [_ slider-props] [slider-structure slider-id slider-props])}))

(defn element
  ; @warning
  ; UNFINISHED! DO NOT USE!
  ;
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
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :resetable? (boolean)(opt)
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)(opt)
  ;  :width (keyword)(opt)
  ;   :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto}
  ;
  ; @usage
  ; [slider {...}]
  ;
  ; @usage
  ; [slider :my-slider {...}]
  ([slider-props]
   [element (random/generate-keyword) slider-props])

  ([slider-id slider-props]
   (fn [_ slider-props] ; XXX#0106 (README.md#parametering)
       (let [slider-props (pretty-presets/apply-preset                        slider-props)
             slider-props (slider.prototypes/slider-props-prototype slider-id slider-props)]
            [slider slider-id slider-props]))))
