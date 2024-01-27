
(ns pretty-diagrams.line-diagram.views
    (:require [fruits.hiccup.api                       :as hiccup]
              [fruits.random.api                       :as random]
              [pretty-diagrams.line-diagram.attributes :as line-diagram.attributes]
              [pretty-diagrams.line-diagram.prototypes :as line-diagram.prototypes]
              [pretty-presets.api                      :as pretty-presets]
              [pretty-engine.api :as pretty-engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  [diagram-id {:keys [sections] :as diagram-props}]
  [:div (line-diagram.attributes/diagram-attributes diagram-id diagram-props)
        (letfn [(f0 [section-props] (let [section-props (line-diagram.prototypes/section-props-prototype section-props)]
                                         [:div (line-diagram.attributes/diagram-section-attributes diagram-id diagram-props section-props)]))]
               [:div (line-diagram.attributes/diagram-sections-attributes diagram-id diagram-props)
                     (hiccup/put-with [:<>] sections f0)])])

(defn diagram
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :sections (maps in vector)}
  ;   [{:color (keyword or string)(opt)
  ;      :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: primary
  ;     :label (metamorphic-content)(opt)
  ;     :value (integer)}]
  ;  :strength (px)(opt)
  ;    Default: 2
  ;    Min: 1
  ;    Max: 6
  ;  :theme (keyword)(opt)
  ;  :total-value (integer)(opt)
  ;   Default: Sum of the section values
  ;  :width (keyword, px or string)(opt)
  ;   auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto}
  ;
  ; @usage
  ; [line-diagram {...}]
  ;
  ; @usage
  ; [line-diagram :my-line-diagram {...}]
  ([diagram-props]
   [diagram (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parametering)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets/apply-preset                     diagram-props)
             diagram-props (line-diagram.prototypes/diagram-props-prototype diagram-props)]
            [line-diagram diagram-id diagram-props]))))
