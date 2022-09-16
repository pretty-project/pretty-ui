
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.point-diagram
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.math           :as math]
              [mid-fruits.random         :as random]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



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
  [:div.x-point-diagram
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
  ;    Only w/ {:label ...}
  ;   :points (integers in vector)
  ;   :strength (px)(opt)
  ;     Default: 2
  ;     Min: 1
  ;     Max: 6}
  ;
  ; @usage
  ;  [elements/line-diagram {...}]
  ;
  ; @usage
  ;  [elements/point-diagram :my-point-diagram {...}]
  ([diagram-props]
   [element (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (diagram-props-prototype diagram-props)]
        [point-diagram diagram-id diagram-props])))
