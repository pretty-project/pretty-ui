
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.prototypes
    (:require [mid-fruits.candy                 :refer [param]]
              [plugins.item-viewer.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:item-path (vector)}
  [viewer-id body-props]
  (merge {:item-path (core.helpers/default-item-path viewer-id)}
         (param body-props)))
