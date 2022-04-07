
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.prototypes
    (:require [mid-fruits.candy                  :refer [param]]
              [plugins.item-browser.core.config  :as core.config]
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
  ;   :items-key (keyword)
  ;   :items-path (vector)
  ;   :label-key (keyword)
  ;   :path-key (keyword)}
  [browser-id body-props]
  (merge {:item-path  (core.helpers/default-item-path  browser-id)
          :items-path (core.helpers/default-items-path browser-id)
          :items-key  core.config/DEFAULT-ITEMS-KEY
          :label-key  core.config/DEFAULT-LABEL-KEY
          :path-key   core.config/DEFAULT-PATH-KEY}
         (param body-props)))
