
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  ;  {}
  [surface-id]
  {:class :x-app-surface--element
   :id    (a/dom-value surface-id)
   :key   (a/dom-value surface-id)})
