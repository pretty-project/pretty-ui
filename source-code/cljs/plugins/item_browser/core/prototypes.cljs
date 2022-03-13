
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.prototypes
    (:require [mid-fruits.candy                 :refer [param]]
              [plugins.item-browser.core.config :as core.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:collection-name (string)
  ;   :items-key (keyword)
  ;   :label-key (keyword)
  ;   :path-key (keyword)}
  [extension-id _ body-props]
  (merge {:collection-name (name extension-id)
          :items-key core.config/DEFAULT-ITEMS-KEY
          :label-key core.config/DEFAULT-LABEL-KEY
          :path-key  core.config/DEFAULT-PATH-KEY}
         (param body-props)))
