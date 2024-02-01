
(ns pretty-diagrams.point-diagram.views
    (:require [fruits.random.api                        :as random]
              [fruits.hiccup.api                        :as hiccup]
              [pretty-diagrams.point-diagram.attributes :as point-diagram.attributes]
              [pretty-diagrams.point-diagram.prototypes :as point-diagram.prototypes]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- point-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (integer) datum-dex
  ; @param (*) datum
  [diagram-id diagram-props datum-dex datum]
  [:div (point-diagram.attributes/diagram-datum-attributes diagram-id diagram-props datum-dex datum)])

(defn- point-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  (letfn [(f0 [datum-dex datum] [point-diagram-datum diagram-id diagram-props datum-dex datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data diagram-id diagram-props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- point-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div (point-diagram.attributes/diagram-attributes diagram-id diagram-props)
        [:div (point-diagram.attributes/diagram-body-attributes diagram-id diagram-props)]])
              ; Point diagrams display value pairs (as coordinates)
              ; Use SVG polyline to display points connected with lines.

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-diagrams.engine/diagram-did-mount    diagram-id diagram-props))
                       :component-will-unmount (fn [_ _] (pretty-diagrams.engine/diagram-will-unmount diagram-id diagram-props))
                       :reagent-render         (fn [_ diagram-props] [point-diagram diagram-id diagram-props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:class (keyword or keywords in vector)
  ;  :datum-color-f (function)(opt)
  ;  :datum-label-f (function)(opt)
  ;  :datum-value-f (function)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :get-data-f (function)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-value (number)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :strength (percent)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [point-diagram {...}]
  ;
  ; @usage
  ; [point-diagram :my-point-diagram {...}]
  ([diagram-props]
   [view (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parametering)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets.engine/apply-preset                diagram-id diagram-props)
             diagram-props (point-diagram.prototypes/diagram-props-prototype  diagram-id diagram-props)
             diagram-props (pretty-diagrams.engine/calculate-diagram-data-sum diagram-id diagram-props)]
            [view-lifecycles diagram-id diagram-props]))))
