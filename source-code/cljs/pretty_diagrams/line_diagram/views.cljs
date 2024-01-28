
(ns pretty-diagrams.line-diagram.views
    (:require [fruits.hiccup.api                       :as hiccup]
              [fruits.random.api                       :as random]
              [pretty-diagrams.line-diagram.attributes :as line-diagram.attributes]
              [pretty-diagrams.line-diagram.prototypes :as line-diagram.prototypes]
              [pretty-engine.api                       :as pretty-engine]
              [pretty-presets.api                      :as pretty-presets]
              [reagent.api                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-diagram
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-lifecycles
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-engine/diagram-did-mount    diagram-id diagram-props))
                       :component-will-unmount (fn [_ _] (pretty-engine/diagram-will-unmount diagram-id diagram-props))
                       :reagent-render         (fn [_ diagram-props] [line-diagram diagram-id diagram-props])}))

(defn diagram
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :sections (maps in vector)}
  ;   [{:color (keyword or string)(opt)
  ;      :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: primary
  ;     :label (metamorphic-content)(opt)
  ;     :value (integer)(opt)}]
  ;  :strength (px)(opt)
  ;    Default: 2
  ;    Min: 1
  ;    Max: 6
  ;  :style (map)(opt)
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
       (let [diagram-props (pretty-presets/apply-preset                     diagram-id diagram-props)
             diagram-props (line-diagram.prototypes/diagram-props-prototype diagram-id diagram-props)]
            [diagram-lifecycles diagram-id diagram-props]))))
