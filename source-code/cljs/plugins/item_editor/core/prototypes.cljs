
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.prototypes
    (:require [mid-fruits.candy                 :refer [param]]
              [plugins.item-editor.core.helpers :as core.helpers]))



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
  ;   :suggestions-path (vector)}
  [extension-id item-namespace body-props]
  (merge {:collection-name  (name extension-id)
          :item-path        (core.helpers/default-item-path        extension-id item-namespace)
          :suggestions-path (core.helpers/default-suggestions-path extension-id item-namespace)}
         (param body-props)))
