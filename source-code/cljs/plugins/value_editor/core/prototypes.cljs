
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.core.prototypes
    (:require [plugins.value-editor.core.helpers :as core.helpers]
              [mid-fruits.candy                  :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  ;  {:edit-path (item-path vector)
  ;   :required? (boolean)
  ;   :save-button-label (metamorphic-content)(opt)
  ;   :value-path (item-path vector)}
  [extension-id editor-id {:keys [edit-original? value-path] :as editor-props}]
  (merge {:required?          true
          :save-button-label :save!
          :edit-path  (core.helpers/default-edit-path extension-id editor-id)
          :value-path (core.helpers/default-edit-path extension-id editor-id)}
         (param editor-props)
         (if edit-original? {:edit-path value-path})))