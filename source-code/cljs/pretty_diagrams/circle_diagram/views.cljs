
(ns pretty-diagrams.circle-diagram.views
    (:require [fruits.hiccup.api                         :as hiccup]
              [fruits.random.api                         :as random]
              [fruits.svg.api                            :as svg]
              [pretty-diagrams.circle-diagram.attributes :as circle-diagram.attributes]
              [pretty-diagrams.circle-diagram.prototypes :as circle-diagram.prototypes]
              [pretty-diagrams.engine.api                :as pretty-diagrams.engine]
              [pretty-presets.engine.api                 :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- circle-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (integer) datum-dex
  ; @param (map) datum-props
  [diagram-id diagram-props datum-dex datum-props]
  [:circle (circle-diagram.attributes/diagram-datum-attributes diagram-id diagram-props datum-dex datum-props)])

(defn- circle-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  (letfn [(f0 [datum-dex datum] [circle-diagram-datum diagram-id diagram-props datum-dex datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data diagram-id diagram-props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- circle-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div (circle-diagram.attributes/diagram-attributes diagram-id diagram-props)
        [:div (circle-diagram.attributes/diagram-inner-attributes diagram-id diagram-props)
              [:svg (svg/wrapper-attributes {:height 200 :width 200})
                    [circle-diagram-datum-list diagram-id diagram-props]]]])

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
                         :reagent-render         (fn [_ diagram-props] [circle-diagram diagram-id diagram-props])}))

(defn view
  ; @description
  ; Circle diagram for displaying values with colored circle sections.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Data properties](pretty-core/cljs/pretty-properties/api.html#data-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Shape properties](pretty-core/cljs/pretty-properties/api.html#shape-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-diagrams/circle-diagram.png)
  ; (def DATA [{:color :primary :value 10} {:color :secondary :value 10} {:color :muted :value 10} {:color :highlight :value 30}])
  ;
  ; [circle-diagram {:datum-color-f :color
  ;                  :datum-value-f :value
  ;                  :get-data-f    #(-> DATA)
  ;                  :outer-height  :m
  ;                  :outer-width   :m
  ;                  :strength      50}]
  ([diagram-props]
   [view (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parameterizing)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets.engine/apply-preset                diagram-id diagram-props)
             diagram-props (circle-diagram.prototypes/diagram-props-prototype diagram-id diagram-props)
             diagram-props (pretty-diagrams.engine/calculate-diagram-data-sum diagram-id diagram-props)]
            [view-lifecycles diagram-id diagram-props]))))
