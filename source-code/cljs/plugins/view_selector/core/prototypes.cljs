
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.prototypes
    (:require [mid-fruits.candy                  :refer [param]]
              [plugins.view-selector.core.config :as core.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (map)
  ;  {:default-view-id (keyword)}
  [view-props]
  (merge {:default-view-id core.config/DEFAULT-VIEW-ID}
         (param view-props)))
