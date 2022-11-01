
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.body.prototypes
    (:require [engines.file-editor.core.helpers :as core.helpers]
              [mid-fruits.candy                 :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:content-path (vector)
  ;   :transfer-id (keyword)}
  [editor-id body-props]
  (merge {:content-path (core.helpers/default-content-path editor-id)
          ; XXX#8173
          :transfer-id editor-id}
         (param body-props)))
