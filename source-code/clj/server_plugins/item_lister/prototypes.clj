
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.23
; Description:
; Version: v0.5.0
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.prototypes
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
              [x.server-user.api :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn updated-item-prototype
  ; @param (map) env
  ;  {:request (map)}
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced map) updated-item
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (object)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (object)
  ;   :namespace/modified-by (map)}
  [{:keys [request]} _ item-namespace updated-item]
  (let [namespace (name item-namespace)
        timestamp (time/timestamp-object)
        user-link (user/request->user-link request)]
       (merge {(keyword namespace "added-at") timestamp
               (keyword namespace "added-by") user-link}
              (param updated-item)
              {(keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))
