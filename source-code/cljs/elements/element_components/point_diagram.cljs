
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element-components.point-diagram
    (:require [elements.engine.api :as engine]
              [mid-fruits.candy    :refer [param]]
              [mid-fruits.css      :as css]
              [mid-fruits.math     :as math]
              [mid-fruits.random   :as random]
              [mid-fruits.vector   :as vector]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [{:keys [] :as diagram-props}]
  (merge {}
         (param diagram-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- point-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.e-point-diagram
    {:style {:width "500px" :height "300px"}}
    (str diagram-props)
    [:svg {:style {:width "100%" :height "100%"
                                 :preserve-aspect-ratio "none"
                                 :view-box              "0 0 100 100"}}
          [:polyline {:points "0,100 100,1"
                      :style  {:fill "none" :stroke "red" :stroke-width "2px"}}]]])


(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ;  {:color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary
  ;    Default: :default
  ;    W/ {:label ...}
  ;   :points (integers in vector)
  ;   :strength (px)(opt)
  ;     Default: 2
  ;     Min: 1
  ;     Max: 6}
  ;
  ; @usage
  ;  [line-diagram {...}]
  ;
  ; @usage
  ;  [point-diagram :my-point-diagram {...}]
  ([diagram-props]
   [element (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (diagram-props-prototype diagram-props)]
        [point-diagram diagram-id diagram-props])))
