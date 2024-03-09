
(ns pretty-diagrams.circle-diagram.views
    (:require [fruits.hiccup.api                         :as hiccup]
              [fruits.random.api                         :as random]
              [fruits.svg.api                            :as svg]
              [pretty-diagrams.circle-diagram.attributes :as circle-diagram.attributes]
              [pretty-diagrams.circle-diagram.prototypes :as circle-diagram.prototypes]
              [pretty-diagrams.engine.api                :as pretty-diagrams.engine]
              [pretty-diagrams.methods.api               :as pretty-diagrams.methods]
              [reagent.core                              :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- circle-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (integer) dex
  ; @param (map) datum-props
  [id props dex datum-props]
  [:circle (circle-diagram.attributes/datum-attributes id props dex datum-props)])

(defn- circle-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  (letfn [(f0 [dex datum] [circle-diagram-datum id props dex datum])]
         (let [data (pretty-diagrams.engine/get-diagram-data id props)]
              (hiccup/put-with-indexed [:<>] data f0))))

(defn- circle-diagram
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (circle-diagram.attributes/outer-attributes id props)
        [:div (circle-diagram.attributes/inner-attributes id props)
              [:svg (svg/wrapper-attributes {:height 200 :width 200})
                    [circle-diagram-datum-list id props]]]])

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
                         :reagent-render         (fn [_ props] [circle-diagram id props])}))

(defn view
  ; @description
  ; Circle diagram for displaying values with colored circle sections.
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ; [Shape model](pretty-core/cljs/pretty-models/api.html#shape-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
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
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-diagrams.methods/apply-diagram-preset         id props)
             props (pretty-diagrams.methods/import-diagram-dynamic-props id props)
             props (pretty-diagrams.methods/import-diagram-data-sum      id props)
             props (pretty-diagrams.methods/import-diagram-state-events  id props)
             props (pretty-diagrams.methods/import-diagram-state         id props)
             props (circle-diagram.prototypes/props-prototype            id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
