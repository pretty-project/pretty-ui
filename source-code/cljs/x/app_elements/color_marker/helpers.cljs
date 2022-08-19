
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-marker.helpers
    (:require [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [marker-id {:keys [size] :as marker-props}]
  (merge (engine/element-default-attributes marker-id marker-props)
         (engine/element-indent-attributes  marker-id marker-props)
         {:data-size size}))

(defn marker-color-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ; @param (keyword or string) color
  ;
  ; @return (map)
  ;  {}
  [_ _ color]
  (cond (keyword? color) {:data-color color}
        (string?  color) {:style {:background-color color}}))
