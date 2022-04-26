
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.prototypes
    (:require [mid-fruits.candy                  :refer [param]]
              [plugins.item-browser.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:item-path (vector)
  ;   :items-path (vector)}
  [browser-id body-props]
  (merge {:item-path  (core.helpers/default-item-path  browser-id)
          :items-path (core.helpers/default-items-path browser-id)}
         (param body-props)))
