
(ns pretty-diagrams.line-diagram.views
    (:require [fruits.hiccup.api                       :as hiccup]
              [fruits.random.api                       :as random]
              [pretty-diagrams.engine.api              :as pretty-diagrams.engine]
              [pretty-diagrams.line-diagram.attributes :as line-diagram.attributes]
              [pretty-diagrams.line-diagram.prototypes :as line-diagram.prototypes]
              [pretty-diagrams.methods.api             :as pretty-diagrams.methods]
              [reagent.core                            :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (integer) dex
  ; @param (*) datum
  [id props dex datum]
  [:div (line-diagram.attributes/datum-attributes id props dex datum)])

(defn- line-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  (letfn [(f0 [dex datum] [line-diagram-datum id props dex datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data id props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- line-diagram
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (line-diagram.attributes/outer-attributes id props)
        [:div (line-diagram.attributes/inner-attributes id props)
              [line-diagram-datum-list                  id props]]])

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
                         :reagent-render         (fn [_ props] [line-diagram id props])}))

(defn view
  ; @description
  ; Line diagram for displaying values on a continuous colored line.
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
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
  ; @usage (pretty-diagrams/line-diagram.png)
  ; (def DATA [{:color :primary :value 10} {:color :secondary :value 10} {:color :muted :value 10} {:color :highlight :value 30}])
  ;
  ; [line-diagram {:datum-color-f :color
  ;                :datum-value-f :value
  ;                :get-data-f    #(-> DATA)
  ;                :outer-height  :m
  ;                :outer-width   :m
  ;                :strength      100}]
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
             props (line-diagram.prototypes/props-prototype              id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
