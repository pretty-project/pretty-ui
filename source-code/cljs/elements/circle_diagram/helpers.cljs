
(ns elements.circle-diagram.helpers
    (:require [css.api                        :as css]
              [math.api                       :as math]
              [elements.circle-diagram.config :as circle-diagram.config]
              [elements.element.helpers       :as element.helpers]))
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props<-total-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  ;
  ; @example
  ; (diagram-props<-total-value {:sections [{:value 50} {:value 25} {:value 50}]})
  ; =>
  ; {:sections [{:value 25 :sum 0} {:value 25 :sum 25} {:value 50 :sum 50}] :total-value 100}
  ;
  ; @return (map)
  ; {:sections (maps in vector)
  ;   [{:sum (integer)}]
  ;  :total-value (integer)}
  [{:keys [sections] :as diagram-props}]
  ; XXX#1218
  ; This function iterates over the sections (from the 'diagram-props' map) ...
  ; ... and calculates the total value of all sections.
  ; ... and calculates the previous sections summary value of each section.
  ;
  ; The element needs ...
  ; ... the total value to calculates how the sections related to the total.
  ; ... the previous sections summary of each section to calculates how
  ;     a section has to be rotated.
  (letfn [(f [{:keys [total-value] :as diagram-props} dex {:keys [value]}]
             (-> diagram-props (update   :total-value + value)
                               (assoc-in [:sections dex :sum] (or total-value 0))))]
         (reduce-kv f diagram-props sections)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn section-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
        :class :e-circle-diagram--section
        :data-stroke-color color
        :style {:stroke-dasharray (str dash-filled " " dash-empty)
                :stroke-width     (css/px     strength)
                :transform        (css/rotate rotation)}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)
  ;   {:height (string)
  ;    :width (string)}}
  [diagram-id {:keys [diameter style] :as diagram-props}]
  (merge (element.helpers/element-indent-attributes diagram-id diagram-props)
         {:style (merge style {:height (css/px diameter)
                               :width  (css/px diameter)})}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [diagram-id diagram-props]
  (merge (element.helpers/element-default-attributes diagram-id diagram-props)
         (element.helpers/element-outdent-attributes diagram-id diagram-props)))
