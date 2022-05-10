
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.ghost.helpers
    (:require [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [ghost-id {:keys [height] :as ghost-props}]
  (merge (engine/element-default-attributes ghost-id ghost-props)
         (engine/element-indent-attributes  ghost-id ghost-props)
         {:data-height height}))
