
(ns pretty-diagrams.circle-diagram.views
    (:require [fruits.hiccup.api                         :as hiccup]
              [fruits.random.api                         :as random]
              [fruits.svg.api                            :as svg]
              [pretty-diagrams.circle-diagram.attributes :as circle-diagram.attributes]
              [pretty-diagrams.circle-diagram.prototypes :as circle-diagram.prototypes]
              [pretty-engine.api                         :as pretty-engine]
              [pretty-presets.api                        :as pretty-presets]
              [reagent.api                               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- circle-diagram-section
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (integer) section-dex
  ; @param (map) section-props
  [diagram-id diagram-props section-dex section-props]
  (let [section-props (circle-diagram.prototypes/section-props-prototype section-dex section-props)]
       [:circle (circle-diagram.attributes/diagram-section-attributes diagram-id diagram-props section-dex section-props)]))

(defn- circle-diagram-section-list
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  [diagram-id {:keys [sections] :as diagram-props}]
  (letfn [(f0 [section-dex section-props] [circle-diagram-section diagram-id diagram-props section-dex section-props])]
         (hiccup/put-with-indexed [:<>] sections f0)))

(defn- circle-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)}
  [diagram-id {:keys [diameter] :as diagram-props}]
  ; The SVG container element prevents the SVG element to shrink if the indent property of the body element is set.
  [:div (circle-diagram.attributes/diagram-attributes diagram-id diagram-props)
        [:div (circle-diagram.attributes/diagram-body-attributes diagram-id diagram-props)
              [:div (circle-diagram.attributes/diagram-svg-container-attributes diagram-id diagram-props)
                    [:svg (svg/wrapper-attributes {:height diameter :width diameter})
                          [circle-diagram-section-list diagram-id diagram-props]]]]])

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
                       :reagent-render         (fn [_ diagram-props] [circle-diagram diagram-id diagram-props])}))

(defn diagram
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :diameter (px)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :sections (maps in vector)}
  ;   [{:color (keyword or string)(opt)
  ;     :label (metamorphic-content)(opt)
  ;     :value (integer)(opt)}]
  ;  :strength (px)(opt)
  ;    Min: 1
  ;    Max: 6
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [circle-diagram {...}]
  ;
  ; @usage
  ; [circle-diagram :my-circle-diagram {...}]
  ([diagram-props]
   [diagram (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parametering)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets/apply-preset                       diagram-id diagram-props)
             diagram-props (circle-diagram.prototypes/diagram-props-prototype diagram-id diagram-props)]
            [diagram-lifecycles diagram-id diagram-props]))))
