
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.prototypes
    (:require [mid-fruits.candy                :refer [param]]
              [plugins.item-lister.core.config :as core.config]))



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
  ;   :download-limit (integer)
  ;   :order-by-options (namespaced keywords in vector)
  ;   :search-keys (keywords in vector)}
  [extension-id _ body-props]
  (merge {:collection-name  (name extension-id)
          :download-limit   core.config/DEFAULT-DOWNLOAD-LIMIT
          :order-by-options core.config/DEFAULT-ORDER-BY-OPTIONS
          :search-keys      core.config/DEFAULT-SEARCH-KEYS}
         (param body-props)))
