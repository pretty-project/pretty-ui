
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.flex-handler.helpers
    (:require [x.app-elements.engine.element :as element]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn flexible-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:wrap-items? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-wrap-items (boolean)}
  [element-id {:keys [wrap-items?] :as element-props}]
  (cond-> (element/element-attributes element-id element-props)
          (some? wrap-items?) (assoc :data-wrap-items (boolean wrap-items?))))
