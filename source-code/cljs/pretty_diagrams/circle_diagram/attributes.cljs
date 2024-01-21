
(ns pretty-diagrams.circle-diagram.attributes
    (:require [fruits.css.api                        :as css]
              [fruits.math.api                       :as math]
              [pretty-build-kit.api                  :as pretty-build-kit]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-section-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)
  ;  :strength (px)
  ;  :total-value (integer)}
  ; @param (map) section-props
  ; {:color (keyword or string)
  ;  :sub (integer)
  ;  :value (integer)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [diameter strength total-value] :as diagram-props} {:keys [color sum value]}]
  (let [x  (/ diameter 2)
        y  (/ diameter 2)
        r  (/ (- diameter strength) 2)
        cf (* 2 r math/PI)
        value-ratio      (math/percent total-value value)
        dash-filled      (* cf (/ value-ratio 100))
        dash-empty       (- cf dash-filled)
        rotation-percent (math/percent total-value sum)
        rotation-angle   (math/percent->angle rotation-percent)
        rotation         (+ rotation-angle circle-diagram.config/ANGLE-CORRECTION)]
       {:cx x :cy y :r r
        :class :pd-circle-diagram--section
        :data-stroke-color color
        :style {:stroke-dasharray (str dash-filled " " dash-empty)
                :stroke-width     (css/px     strength)
                :transform        (css/rotate rotation)}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-svg-container-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)
  ;   {:height (string)
  ;    :width (string)}}
  [_ {:keys [diameter] :as diagram-props}]
  (-> {:class :pd-circle-diagram--svg-container
       :style {:height (css/px diameter)
               :width  (css/px diameter)}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------


(defn diagram-body-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ diagram-props]
  (-> {:class :pd-circle-diagram--body}
      (pretty-build-kit/indent-attributes diagram-props)
      (pretty-build-kit/style-attributes  diagram-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {}
  [_ diagram-props]
  (-> {:class :pd-circle-diagram}
      (pretty-build-kit/class-attributes   diagram-props)
      (pretty-build-kit/outdent-attributes diagram-props)
      (pretty-build-kit/state-attributes   diagram-props)))
