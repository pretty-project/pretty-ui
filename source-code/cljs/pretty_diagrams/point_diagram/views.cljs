
(ns pretty-diagrams.point-diagram.views
    (:require [fruits.random.api                        :as random]
              [pretty-diagrams.point-diagram.attributes :as point-diagram.attributes]
              [pretty-diagrams.point-diagram.prototypes :as point-diagram.prototypes]
              [pretty-presets.api                       :as pretty-presets]
              [pretty-engine.api :as pretty-engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- point-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div (point-diagram.attributes/diagram-attributes diagram-id diagram-props)
        [:div (point-diagram.attributes/diagram-body-attributes diagram-id diagram-props)
              [:svg {:style {:width "100%" :height "100%"
                                           :preserve-aspect-ratio "none"
                                           :view-box              "0 0 100 100"}}
                    [:polyline {:points "0,100 100,1"
                                :style  {:fill "none" :stroke "red" :stroke-width "2px"}}]]]])

(defn diagram
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;   W/ {:label ...}
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :points (integers in vector)
  ;  :preset (keyword)(opt)
  ;  :strength (px)(opt)
  ;    Default: 2
  ;    Min: 1
  ;    Max: 6
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [point-diagram {...}]
  ;
  ; @usage
  ; [point-diagram :my-point-diagram {...}]
  ([diagram-props]
   [diagram (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   ; @note (tutorials#parametering)
   (fn [_ diagram-props]
       (let [diagram-props (pretty-presets/apply-preset diagram-props)]
             ; diagram-props (point-diagram.prototypes/diagram-props-prototype diagram-props)
            [point-diagram diagram-id diagram-props]))))
