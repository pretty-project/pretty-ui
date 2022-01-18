
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.4
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.subs
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-router.api :as router]
              [app-plugins.item-browser.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id _ item-key]]
  (get-in db [extension-id :item-browser/meta-items item-key]))

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:default-item-id (keyword)(opt)}
  ;
  ; @return (string)
  ;  Az item-id forrásából (route-path param) származó adat. Annak hiánya esetén a default-item-id.
  [db [_ extension-id item-namespace {:keys [default-item-id]}]]
  (if-let [derived-item-id (r router/get-current-route-path-param db :item-id)]
          (return derived-item-id)
          (return default-item-id)))

(defn get-current-item-id
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r item-browser/get-current-item-id db :my-extension)
  ;
  ; @return (string)
  [db [_ extension-id]]
  (get-in db [extension-id :item-browser/meta-items :item-id]))

(defn get-current-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (vector)
  [db [_ extension-id]]
  (let [current-path (get-in db [extension-id :item-browser/meta-items :current-path])]
       (vec current-path)))

(defn at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [current-path (r get-current-path db extension-id)]
       (empty? current-path)))

(defn get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:at-home? (boolean)}
  [db [_ extension-id item-namespace]]
  (merge ; TEMP
         (r app-plugins.item-lister.subs/get-header-props db extension-id item-namespace)
         {:at-home? (r at-home? db extension-id)}))

(a/reg-sub :item-browser/get-header-props get-header-props)

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; TEMP
  ; Milyen néven legyen beimportolva a subs névtér?
  (r app-plugins.item-lister.subs/get-view-props db extension-id item-namespace))

(a/reg-sub :item-browser/get-view-props get-view-props)
