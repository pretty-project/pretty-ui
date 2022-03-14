
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.prototypes
    (:require [mid-fruits.candy                  :refer [param]]
              [plugins.view-selector.core.config :as core.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:default-view-id (keyword)}
  [body-props]
  (merge {:default-view-id core.config/DEFAULT-VIEW-ID}
         (param body-props)))
