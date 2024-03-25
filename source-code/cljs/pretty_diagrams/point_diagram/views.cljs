
(ns pretty-diagrams.point-diagram.views
    (:require [fruits.hiccup.api                        :as hiccup]
              [fruits.random.api                        :as random]
              [pretty-diagrams.engine.api               :as pretty-diagrams.engine]
              [pretty-diagrams.methods.api              :as pretty-diagrams.methods]
              [pretty-diagrams.point-diagram.attributes :as point-diagram.attributes]
              [pretty-diagrams.point-diagram.prototypes :as point-diagram.prototypes]
              [reagent.core                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- point-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (integer) dex
  ; @param (*) datum
  [id props dex datum]
  [:div (point-diagram.attributes/datum-attributes id props dex datum)])

(defn- point-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  (letfn [(f0 [dex datum] [point-diagram-datum id props dex datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data id props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- point-diagram
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (point-diagram.attributes/outer-attributes id props)
        [:div (point-diagram.attributes/inner-attributes id props)]])
              ; Point diagrams display value pairs (as coordinates)
              ; Use SVG polyline to display points connected with lines (=? graph-diagram).

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-diagrams.engine/diagram-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-diagrams.engine/diagram-will-unmount id props))
                         :reagent-render         (fn [_ props] [point-diagram id props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @description
  ; Point diagram for displaying values with colored dots.
  ;
  ; @links Implemented models
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ; [Shape canvas model](pretty-core/cljs/pretty-models/api.html#shape-canvas-model)
  ;
  ; @links Implemented properties
  ; [Data properties](pretty-core/cljs/pretty-properties/api.html#data-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-diagrams/point-diagram.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-diagrams.methods/apply-diagram-presets        id props)
             props (pretty-diagrams.methods/import-diagram-dynamic-props id props)
             props (pretty-diagrams.methods/import-diagram-data-sum      id props)
             props (pretty-diagrams.methods/import-diagram-state-events  id props)
             props (pretty-diagrams.methods/import-diagram-state         id props)
             props (point-diagram.prototypes/props-prototype             id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
