
(ns pretty-diagrams.bar-diagram.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-diagrams.bar-diagram.attributes :as bar-diagram.attributes]
              [pretty-diagrams.bar-diagram.prototypes :as bar-diagram.prototypes]
              [pretty-diagrams.engine.api             :as pretty-diagrams.engine]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bar-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (integer) datum-dex
  ; @param (*) datum
  [diagram-id diagram-props datum-dex datum]
  [:div (bar-diagram.attributes/diagram-datum-attributes diagram-id diagram-props datum-dex datum)])

(defn- bar-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  (letfn [(f0 [datum-dex datum] [bar-diagram-datum diagram-id diagram-props datum-dex datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data diagram-id diagram-props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- bar-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div (bar-diagram.attributes/diagram-attributes diagram-id diagram-props)
        [:div (bar-diagram.attributes/diagram-body-attributes diagram-id diagram-props)
              [bar-diagram-datum-list                         diagram-id diagram-props]]])

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
                         :reagent-render         (fn [_ diagram-props] [bar-diagram diagram-id diagram-props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @description
  ; Bar diagram for displaying values with colored bars.
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
  ; @usage (pretty-diagrams/bar-diagram.png)
  ; [bar-diagram {:datum-color-f :color
  ;               :datum-value-f :value
  ;               :get-data-f    #(-> [{:color :primary :value 10} {:color :secondary :value 10} {:color :muted :value 10} {:color :highlight :value 30}])
  ;               :strength      100
  ;               :height        :m
  ;               :width         :m}]
  ([diagram-props]
   [view (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parameterizing)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets.engine/apply-preset                diagram-id diagram-props)
             diagram-props (bar-diagram.prototypes/diagram-props-prototype    diagram-id diagram-props)
             diagram-props (pretty-diagrams.engine/calculate-diagram-data-sum diagram-id diagram-props)]
            [view-lifecycles diagram-id diagram-props]))))
