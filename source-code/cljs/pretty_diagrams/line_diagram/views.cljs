
(ns pretty-diagrams.line-diagram.views
    (:require [fruits.hiccup.api                       :as hiccup]
              [fruits.random.api                       :as random]
              [pretty-diagrams.engine.api              :as pretty-diagrams.engine]
              [pretty-diagrams.line-diagram.attributes :as line-diagram.attributes]
              [pretty-diagrams.line-diagram.prototypes :as line-diagram.prototypes]
              [pretty-presets.engine.api               :as pretty-presets.engine]
              [reagent.api                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-diagram-datum
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (integer) datum-dex
  ; @param (*) datum
  [diagram-id diagram-props datum-dex datum]
  [:div (line-diagram.attributes/diagram-datum-attributes diagram-id diagram-props datum-dex datum)])

(defn- line-diagram-datum-list
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  (letfn [(f0 [datum-dex datum] [line-diagram-datum diagram-id diagram-props datum-dex datum])]
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

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-diagrams.engine/diagram-did-mount    diagram-id diagram-props))
                       :component-will-unmount (fn [_ _] (pretty-diagrams.engine/diagram-will-unmount diagram-id diagram-props))
                       :reagent-render         (fn [_ diagram-props] [line-diagram diagram-id diagram-props])}))

(defn view
  ; @description
  ; Line diagram for displaying values with continuous colored lines.
  ;
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :datum-color-f (function)(opt)
  ;  :datum-label-f (function)(opt)
  ;  :datum-value-f (function)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :get-data-f (function)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-value (number)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :strength (percent)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [line-diagram {...}]
  ;
  ; @usage
  ; [line-diagram :my-line-diagram {...}]
  ([diagram-props]
   [view (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parameterizing)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets.engine/apply-preset                diagram-id diagram-props)
             diagram-props (line-diagram.prototypes/diagram-props-prototype   diagram-id diagram-props)
             diagram-props (pretty-diagrams.engine/calculate-diagram-data-sum diagram-id diagram-props)]
            [view-lifecycles diagram-id diagram-props]))))
