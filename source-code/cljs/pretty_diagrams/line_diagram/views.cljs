
(ns pretty-diagrams.line-diagram.views
    (:require [fruits.hiccup.api                       :as hiccup]
              [fruits.random.api                       :as random]
              [pretty-diagrams.engine.api              :as pretty-diagrams.engine]
              [pretty-diagrams.line-diagram.attributes :as line-diagram.attributes]
              [pretty-diagrams.line-diagram.prototypes :as line-diagram.prototypes]
              [pretty-presets.engine.api               :as pretty-presets.engine]
              [reagent.core :as reagent]))

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
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Data properties](pretty-core/cljs/pretty-properties/api.html#data-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Shape properties](pretty-core/cljs/pretty-properties/api.html#shape-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
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
       (let [props (pretty-presets.engine/apply-preset                id props)
             props (line-diagram.prototypes/props-prototype           id props)
             props (pretty-diagrams.engine/calculate-diagram-data-sum id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
