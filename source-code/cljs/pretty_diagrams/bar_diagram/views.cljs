
(ns pretty-diagrams.bar-diagram.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-diagrams.bar-diagram.attributes :as bar-diagram.attributes]
              [pretty-diagrams.bar-diagram.prototypes :as bar-diagram.prototypes]
              [pretty-diagrams.engine.api             :as pretty-diagrams.engine]
              [pretty-diagrams.methods.api            :as pretty-diagrams.methods]
              [reagent.core                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bar-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (integer) dex
  ; @param (*) datum
  [id props dex datum]
  [:div (bar-diagram.attributes/datum-attributes id props dex datum)])

(defn- bar-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  (letfn [(f0 [dex datum] [bar-diagram-datum id props dex datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data id props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- bar-diagram
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (bar-diagram.attributes/outer-attributes id props)
        [:div (bar-diagram.attributes/inner-attributes id props)
              [bar-diagram-datum-list                  id props]]])

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
                         :reagent-render         (fn [_ props] [bar-diagram id props])}))

(defn view
  ; @description
  ; Bar diagram for displaying values with colored bars.
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
  ; @usage (pretty-diagrams/bar-diagram.png)
  ; (def DATA [{:color :primary :value 10} {:color :secondary :value 10} {:color :muted :value 10} {:color :highlight :value 30}])
  ;
  ; [bar-diagram {:datum-color-f :color
  ;               :datum-value-f :value
  ;               :get-data-f    #(-> DATA)
  ;               :outer-height  :m
  ;               :outer-width   :m
  ;               :strength      100}]
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
             props (bar-diagram.prototypes/props-prototype               id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
