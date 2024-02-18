
(ns pretty-diagrams.point-diagram.views
    (:require [fruits.hiccup.api                        :as hiccup]
              [fruits.random.api                        :as random]
              [pretty-diagrams.engine.api               :as pretty-diagrams.engine]
              [pretty-diagrams.point-diagram.attributes :as point-diagram.attributes]
              [pretty-diagrams.point-diagram.prototypes :as point-diagram.prototypes]
              [pretty-presets.engine.api                :as pretty-presets.engine]
              [reagent.core :as reagent]))

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
              ; Use SVG polyline to display points connected with lines (=? graph-diagram).

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-diagrams.engine/diagram-did-mount    diagram-id diagram-props))
                         :component-will-unmount (fn [_ _] (pretty-diagrams.engine/diagram-will-unmount diagram-id diagram-props))
                         :reagent-render         (fn [_ diagram-props] [point-diagram diagram-id diagram-props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @description
  ; Point diagram for displaying values with colored dots.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Data properties](pretty-core/cljs/pretty-properties/api.html#data-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Shape properties](pretty-core/cljs/pretty-properties/api.html#shape-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-diagrams/point-diagram.png)
  ; ...
  ([diagram-props]
   [view (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parameterizing)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets.engine/apply-preset                diagram-id diagram-props)
             diagram-props (point-diagram.prototypes/diagram-props-prototype  diagram-id diagram-props)
             diagram-props (pretty-diagrams.engine/calculate-diagram-data-sum diagram-id diagram-props)]
            [view-lifecycles diagram-id diagram-props]))))
