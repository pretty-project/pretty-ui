
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element-components.circle-diagram
    (:require [elements.engine.api :as engine]
              [mid-fruits.candy    :refer [param]]
              [mid-fruits.css      :as css]
              [mid-fruits.math     :as math]
              [mid-fruits.random   :as random]
              [mid-fruits.svg      :as svg]
              [mid-fruits.vector   :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (°)
(def ANGLE-CORRECTION -90)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-props<-total-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  ;
  ; @example
  ;  (diagram-props<-total-value {:sections [{:value 50} {:value 25} {:value 50}]})
  ;  =>
  ;  {:sections [{:value 25 :sum 0} {:value 25 :sum 25} {:value 50 :sum 50}] :total-value 100}
  ;
  ; @return (map)
  ;  {:sections (maps in vector)
  ;    [{:sum (integer)}]
  ;   :total-value (integer)}
  [{:keys [sections] :as diagram-props}]
  ; A diagram-props térkép :sections vektorában felsorolt szekciók térképeibe írja az aktuális
  ; szekció előtti szekciók értékeinek összegét, amelyből majd lehetséges kiszámítani
  ; egyes szekciók elforgatásának mértékét.
  (letfn [(f [{:keys [total-value] :as diagram-props} dex {:keys [value]}]
             (-> diagram-props (update   :total-value + value)
                               (assoc-in [:sections dex :sum] (or total-value 0))))]
         (reduce-kv f diagram-props sections)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:strength (integer)(opt)}
  ;
  ; @return (map)
  ;  {:diameter (px)
  ;   :layout (keyword)
  ;   :strength (px)}
  [{:keys [strength] :as diagram-props}]
  (merge {:diameter 48
          :strength  2
          :layout :fit}
         (diagram-props<-total-value diagram-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- circle-diagram-section
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:diameter (px)
  ;   :strength (px)
  ;   :total-value (integer)}
  ; @param (map) section-props
  ;  {:color (keyword or string)
  ;   :sub (integer)
  ;   :value (integer)}
  [_ {:keys [diameter strength total-value] :as diagram-props} {:keys [color sum value]}]
  (let [x  (/ diameter 2)
        y  (/ diameter 2)
        r  (/ (- diameter strength) 2)
        cf (* 2 r math/pi)
        value-ratio      (math/percent total-value value)
        dash-filled      (* cf (/ value-ratio 100))
        dash-empty       (- cf dash-filled)
        rotation-percent (math/percent total-value sum)
        rotation-angle   (math/percent->angle rotation-percent)
        rotation         (+ rotation-angle ANGLE-CORRECTION)]
       [:circle {:cx x :cy y :r r
                 :class :e-circle-diagram--section
                 :data-color color
                 :style {:stroke-dasharray (str dash-filled " " dash-empty)
                         :stroke-width     (css/px strength)
                         :transform        (css/rotate rotation)}}]))

(defn- circle-diagram-sections
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  [diagram-id {:keys [sections] :as diagram-props}]
  (reduce (fn [circle-diagram-sections section-props]
              (conj circle-diagram-sections [circle-diagram-section diagram-id diagram-props section-props]))
          [:<>] sections))

(defn circle-diagram-circle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:diameter (px)}
  [diagram-id {:keys [diameter] :as diagram-props}]
  [:div.e-circle-diagram--svg {:style {:height (css/px diameter)
                                       :width  (css/px diameter)}}
                              [:svg (svg/wrapper-attributes  {:height diameter :width diameter})
                                    (circle-diagram-sections diagram-id diagram-props)]])

(defn- circle-diagram-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.e-circle-diagram--label]))

(defn circle-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.e-circle-diagram (engine/element-attributes diagram-id diagram-props)
                         [circle-diagram-label      diagram-id diagram-props]
                         [circle-diagram-circle     diagram-id diagram-props]])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ;  {:diameter (px)(opt)
  ;    Default: 48
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :fit
  ;   :sections (maps in vector)}
  ;    [{:color (keyword or string)
  ;       :default, :highlight, :muted, :primary, :secondary, :success, :warning
  ;      :label (metamorphic-content)(opt)
  ;       TODO ...
  ;      :value (integer)}]
  ;   :strength (px)(opt)
  ;     Default: 2
  ;     Min: 1
  ;     Max: 6}
  ;
  ; @usage
  ;  [circle-diagram {...}]
  ;
  ; @usage
  ;  [circle-diagram :my-circle-diagram {...}]
  ([diagram-props]
   [element (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (diagram-props-prototype diagram-props)]
        [circle-diagram diagram-id diagram-props])))
