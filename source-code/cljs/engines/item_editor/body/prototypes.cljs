
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.body.prototypes
    (:require [candy.api                        :refer [param]]
              [engines.item-editor.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:display-progress? (boolean)
  ;   :item-path (vector)
  ;   :suggestions-path (vector)
  ;   :transfer-id (keyword)}
  [editor-id body-props]
  (merge {:display-progress? true
          :item-path         (core.helpers/default-item-path        editor-id)
          :suggestions-path  (core.helpers/default-suggestions-path editor-id)
          ; XXX#8173
          :transfer-id editor-id}
         (param body-props)))
