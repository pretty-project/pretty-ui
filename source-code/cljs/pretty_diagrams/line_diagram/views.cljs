
(ns pretty-diagrams.line-diagram.views
    (:require [fruits.hiccup.api                       :as hiccup]
              [fruits.random.api                       :as random]
              [pretty-diagrams.line-diagram.attributes :as line-diagram.attributes]
              [pretty-diagrams.line-diagram.prototypes :as line-diagram.prototypes]
              [pretty-diagrams.engine.api                       :as pretty-diagrams.engine]
              [pretty-presets.api                      :as pretty-presets]
              [reagent.api                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (*) datum
  [diagram-id diagram-props datum]
  [:div (line-diagram.attributes/diagram-datum-attributes diagram-id diagram-props datum)])

(defn- line-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  (letfn [(f0 [section-dex section-props] [line-diagram-datum diagram-id diagram-props datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data diagram-id diagram-props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- line-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div (line-diagram.attributes/diagram-attributes diagram-id diagram-props)
        [:div (line-diagram.attributes/diagram-body-attributes diagram-id diagram-props)
              [line-diagram-datum-list                         diagram-id diagram-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-lifecycles
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-diagrams.engine/diagram-did-mount    diagram-id diagram-props))
                       :component-will-unmount (fn [_ _] (pretty-diagrams.engine/diagram-will-unmount diagram-id diagram-props))
                       :reagent-render         (fn [_ diagram-props] [line-diagram diagram-id diagram-props])}))

(defn diagram
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:disabled? (boolean)(opt)
  ;  :datum-color-f (function)(opt)
  ;  :datum-label-f (function)(opt)
  ;  :datum-value-f (function)(opt)
  ;  :get-data-f (function)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :sections (maps in vector)(opt)
  ;   [{:color (keyword or string)(opt)
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
  ;  :width (keyword, px or string)(opt)}
  ;
  ; :get-sections-f
  ; :section-color-f
  ; :section-label-f
  ; :section-value-f
  ; :strength (px)
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
