
(ns pretty-diagrams.bar-diagram.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-diagrams.bar-diagram.attributes :as bar-diagram.attributes]
              [pretty-diagrams.bar-diagram.prototypes :as bar-diagram.prototypes]
              [pretty-diagrams.engine.api             :as pretty-diagrams.engine]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [reagent.api                            :as reagent]))

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
        [:div (bar-diagram.attributes/diagram-body-attributes diagram-id diagram-props)]])
              ; Bar diagrams display multiple lines or bars in a stack.

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
                       :reagent-render         (fn [_ diagram-props] [bar-diagram diagram-id diagram-props])}))

(defn view
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @description
  ; Bar diagram for displaying values with colored bars.
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
  ; [bar-diagram {...}]
  ;
  ; @usage
  ; [bar-diagram :my-bar-diagram {...}]
  ([diagram-props]
   [view (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parameterizing)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets.engine/apply-preset                diagram-id diagram-props)
             diagram-props (bar-diagram.prototypes/diagram-props-prototype    diagram-id diagram-props)
             diagram-props (pretty-diagrams.engine/calculate-diagram-data-sum diagram-id diagram-props)]
            [view-lifecycles diagram-id diagram-props]))))
