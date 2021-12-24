
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.07
; Description:
; Version: v1.0.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.circle-diagram
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.loop           :refer [reduce+wormhole]]
              [mid-fruits.math           :as math]
              [mid-fruits.svg            :as svg]
              [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
(def ANGLE-CORRECTION -90)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-props<-rotations
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  ;
  ; @example
  ;  (diagram-props<-rotations {:sections [{:value 50} {:value 25} {:value 50}] :total-value 100})
  ;  =>
  ;  {:sections [{:value 25 :rotation 0} {:value 25 :rotation 90} {:value 50 :rotation 180}] :total-value 100}
  ;
  ; @return (map)
  ;  {:sections (maps in vector)
  ;   [{:rotation (integer)}]}
  [{:keys [sections total-value] :as diagram-props}]
  ; A diagram-props térkép :sections vektorában felsorolt szekciók térképeibe írja az adott szekció
  ; elforgatásának mértékét, amelyet az azt megelőző szekciók értékéből számít.
  (assoc (param diagram-props) :sections
         (reduce+wormhole (fn [sections {:keys [value] :as section} sum]
                              (let [rotation-percent (math/percent total-value sum)
                                    rotation-angle   (math/percent->angle rotation-percent)
                                    updated-section (assoc section :rotation rotation-angle)]
                                   [(vector/conj-item sections updated-section)
                                    (+ sum value)]))
                          (param [])
                          (param sections)
                          (param 0))))

(defn- diagram-props<-total-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  ;
  ; @return (map)
  ;  {:total-value (integer)}
  [{:keys [sections] :as diagram-props}]
  ; A diagram-props térkép :total-value értékét kiszámítja a :sections vektorban felsorolt szekciók
  ; összeadott :value értékeiből.
  (assoc (param diagram-props) :total-value
         (reduce (fn [total-value {:keys [value]}]
                     (+ total-value value))
                 (param 0)
                 (param sections))))

(defn- section-props->value-ratio
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:total-value (integer)}
  ; @param (map) section-props
  ;  {:value (integer)}
  ;
  ; @return (integer)
  [{:keys [total-value]} {:keys [value]}]
  (math/percent total-value value))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- section-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) section-props
  ;
  ; @return (map)
  ;  {:color (keyword)}
  [section-props]
  (merge {:color :primary}
         (param section-props)))

(defn- diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:strength (integer)(opt)
  ;   :total-value (integer)(opt)}
  ;
  ; @return (map)
  ;  {:diameter (px)
  ;   :layout (keyword)
  ;   :strength (px)
  ;   :total-value (integer)}
  [{:keys [strength total-value] :as diagram-props}]
  (merge {:diameter 48
          :layout   :fit
          :strength 2}
         (param diagram-props)
         (if (some? strength) {:strength (math/between! strength 1 6)})

         ; A szekciók elforgatásának kiszámításához először szükséges kiszámítani
         ; az :total-value értéket.
         (cond-> (param diagram-props)
                 (nil?  total-value)  (diagram-props<-total-value)
                 :calculate-rotations (diagram-props<-rotations))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- circle-diagram-section
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:diameter (px)
  ;   :strength (px)}
  ; @param (map) section-props
  ;
  ; @return (hiccup)
  [_ {:keys [diameter strength] :as diagram-props} {:keys [color rotation] :as section-props}]
  (let [x  (/ diameter 2)
        y  (/ diameter 2)
        r  (/ (- diameter strength) 2)
        cf (* 2 r math/pi)
        value-ratio (section-props->value-ratio diagram-props section-props)
        dash-filled (* cf (/ value-ratio 100))
        dash-empty  (- cf dash-filled)
        rotation    (+ rotation ANGLE-CORRECTION)]
       (svg/circle {:x x :y y :r r
                    :class :x-circle-diagram--section
                    :data-color color
                    :style {:stroke-dasharray (str dash-filled " " dash-empty)
                            :stroke-width     (css/px strength)
                            :transform        (css/rotate rotation)}})))

(defn- circle-diagram-sections
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  ;
  ; @return (hiccup)
  [diagram-id {:keys [sections] :as diagram-props}]
  (vec (reduce (fn [circle-diagram-sections section-props]
                   (let [section-props (section-props-prototype section-props)]
                        (conj circle-diagram-sections [circle-diagram-section diagram-id diagram-props section-props])))
               [] sections)))

(defn circle-diagram-circle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:diameter (px)}
  ;
  ; @return (hiccup)
  [diagram-id {:keys [diameter] :as diagram-props}]
  [:div.x-circle-diagram--svg {:style {:height (css/px diameter)
                                       :width  (css/px diameter)}}
                              (svg/svg {:elements (circle-diagram-sections diagram-id diagram-props)
                                        :height diameter :width diameter})])

(defn- circle-diagram-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  (if (some? label)
      [:div.x-circle-diagram--label]))

(defn circle-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (hiccup)
  [diagram-id diagram-props]
  [:div.x-circle-diagram (engine/element-attributes diagram-id diagram-props)
                         [circle-diagram-label      diagram-id diagram-props]
                         [circle-diagram-circle     diagram-id diagram-props]])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ;  {:diameter (px)(opt)
  ;    Default: 48
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :fit
  ;   :sections (maps in vector)}
  ;    [{:color (keyword)(opt)
  ;       :default, :highlight, :muted, :primary, :secondary, :success, :warning
  ;       Default: primary
  ;      :label (metamorphic-content)(opt)
  ;       TODO ...
  ;      :value (integer)}]
  ;   :strength (px)(opt)
  ;     Default: 2
  ;     Min: 1
  ;     Max: 6
  ;   :total-value (integer)(opt)
  ;    Default: A szakaszok aktuális értékének összege}
  ;
  ; @usage
  ;  [elements/circle-diagram {...}]
  ;
  ; @usage
  ;  [elements/circle-diagram :my-circle-diagram {...}]
  ;
  ; @return (component)
  ([diagram-props]
   [element (a/id) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (diagram-props-prototype diagram-props)]
        [circle-diagram diagram-id diagram-props])))
