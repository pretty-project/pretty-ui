
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.prototypes
    (:require [mid-fruits.candy                 :refer [param]]
              [plugins.item-editor.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:item-path (vector)
  ;   :suggestions-path (vector)}
  [editor-id body-props]
  (merge {:item-path        (core.helpers/default-item-path        editor-id)
          :suggestions-path (core.helpers/default-suggestions-path editor-id)}
         (param body-props)))
