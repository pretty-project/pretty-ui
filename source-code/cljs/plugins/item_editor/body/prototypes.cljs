
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.prototypes
    (:require [mid-fruits.candy                     :refer [param]]
              [plugins.item-editor.core.helpers     :as core.helpers]
              [plugins.item-editor.transfer.helpers :as transfer.helpers]))



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
  ;   :suggestions-path (vector)
  ;   :transfer-id (keyword)}
  [editor-id body-props]
  (merge {:item-path        (core.helpers/default-item-path        editor-id)
          :suggestions-path (core.helpers/default-suggestions-path editor-id)
          :transfer-id      (transfer.helpers/transfer-id          editor-id)}
         (param body-props)))
