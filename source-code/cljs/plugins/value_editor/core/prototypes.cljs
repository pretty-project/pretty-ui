
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;   :value-path (vector)}
  ;
  ; @return (map)
  ;  {:edit-path (vector)
  ;   :required? (boolean)
  ;   :save-button-label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  [editor-id {:keys [edit-original? value-path] :as editor-props}]
  (merge {:required?          true
          :save-button-label :save!
          :edit-path  (core.helpers/default-edit-path editor-id)
          :value-path (core.helpers/default-edit-path editor-id)}
         (param editor-props)
         (if edit-original? {:edit-path value-path})))
