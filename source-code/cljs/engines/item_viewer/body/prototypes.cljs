
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.body.prototypes
    (:require [candy.api                        :refer [param]]
              [engines.item-viewer.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:item-path (vector)
  ;   :transfer-id (keyword)}
  [viewer-id body-props]
  (merge {:item-path (core.helpers/default-item-path viewer-id)
          ; XXX#8173
          :transfer-id viewer-id}
         (param body-props)))
