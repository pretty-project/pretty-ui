
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-stamp.helpers
    (:require [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stamp-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [stamp-id {:keys [size] :as stamp-props}]
  (merge (engine/element-default-attributes stamp-id stamp-props)
         (engine/element-indent-attributes  stamp-id stamp-props)
         {:size size}))

(defn stamp-color-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {}
  ; @param (keyword or string) color
  ;
  ; @return (map)
  ;  {}
  [_ element-props color]
  (cond (keyword? color) {:data-color color}
        (string?  color) {:style {:background-color color}}))
