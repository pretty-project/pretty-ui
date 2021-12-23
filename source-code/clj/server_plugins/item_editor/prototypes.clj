
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.23
; Description:
; Version: v0.4.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.prototypes
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.time    :as time]
              [x.server-db.api    :as db]
              [x.server-user.api  :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn updated-item-prototype
  ; @param (map) env
  ;  {:request (map)}
  ; @param (namespaced map) updated-item
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (object)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (object)
  ;   :namespace/modified-by (map)}
  [{:keys [request]} updated-item]
  (let [namespace (db/document->namespace updated-item)
        timestamp (time/timestamp-object)
        user-link (user/request->user-link request)]
       (merge {(keyword/add-namespace namespace :added-at) timestamp
               (keyword/add-namespace namespace :added-by) user-link}
              (param updated-item)
              {(keyword/add-namespace namespace :modified-at) timestamp
               (keyword/add-namespace namespace :modified-by) user-link})))

(defn duplicated-item-prototype
  ; @param (map) env
  ;  {:request (map)}
  ; @param (namespaced map) duplicated-item
  ;  {:namespace/id (string)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (object)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (object)
  ;   :namespace/modified-by (map)}
  [{:keys [request]} duplicated-item]
  (let [namespace (db/document->namespace duplicated-item)
        timestamp (time/timestamp-object)
        user-link (user/request->user-link request)]
       (merge (dissoc duplicated-item (keyword/add-namespace namespace :id))
              {(keyword/add-namespace namespace :added-at)    timestamp
               (keyword/add-namespace namespace :added-by)    user-link
               (keyword/add-namespace namespace :modified-at) timestamp
               (keyword/add-namespace namespace :modified-by) user-link})))
