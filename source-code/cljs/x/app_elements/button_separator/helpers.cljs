
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button-separator.helpers
    (:require [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [separator-id {:keys [color] :as separator-props}]
  (merge (engine/element-default-attributes separator-id separator-props)
         (engine/element-indent-attributes  separator-id separator-props)
         {:data-color color}))
