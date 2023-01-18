
; WARNING! NOT TESTED! DO NOT USE!

(ns elements.point-diagram.views
    (:require [elements.point-diagram.helpers    :as point-diagram.helpers]
              [elements.point-diagram.prototypes :as point-diagram.prototypes]
              [random.api                        :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- point-diagram-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.e-point-diagram--body (point-diagram.helpers/diagram-body-attributes diagram-id diagram-props)
                              [:svg {:style {:width "100%" :height "100%"
                                                           :preserve-aspect-ratio "none"
                                                           :view-box              "0 0 100 100"}}
                                    [:polyline {:points "0,100 100,1"
                                                :style  {:fill "none" :stroke "red" :stroke-width "2px"}}]]])

(defn- point-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.e-point-diagram (point-diagram.helpers/diagram-attributes diagram-id diagram-props)
                        [point-diagram-body                       diagram-id diagram-props]])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;   W/ {:label ...}
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :points (integers in vector)
  ;  :strength (px)(opt)
  ;    Default: 2
  ;    Min: 1
  ;    Max: 6}
  ;
  ; @usage
  ; [line-diagram {...}]
  ;
  ; @usage
  ; [point-diagram :my-point-diagram {...}]
  ([diagram-props]
   [element (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [] ; diagram-props (point-diagram.prototypes/diagram-props-prototype diagram-props)
        [point-diagram diagram-id diagram-props])))
