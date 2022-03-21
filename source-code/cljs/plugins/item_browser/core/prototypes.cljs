
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.prototypes
    (:require [mid-fruits.candy                  :refer [param]]
              [plugins.item-browser.core.config  :as core.config]
              [plugins.item-browser.core.helpers :as core.helpers]))



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
  ;   :item-path (vector)
  ;   :items-key (keyword)
  ;   :items-path (vector)
  ;   :label-key (keyword)
  ;   :path-key (keyword)}
  [extension-id item-namespace body-props]
  (merge {:collection-name (name extension-id)
          :item-path  (core.helpers/default-item-path  extension-id item-namespace)
          :items-path (core.helpers/default-items-path extension-id item-namespace)
          :items-key core.config/DEFAULT-ITEMS-KEY
          :label-key core.config/DEFAULT-LABEL-KEY
          :path-key  core.config/DEFAULT-PATH-KEY}
         (param body-props)))
