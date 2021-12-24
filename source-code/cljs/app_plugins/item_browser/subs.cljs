
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



;; -- Sbscriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) browser-props
  ;  {:default-item-id (keyword)(opt)}
  ;
  ; @return (keyword)
  ;  Az item-id forrásából (route-path param) származó adat. Annak hiánya esetén a default-item-id.
  [db [_ extension-id {:keys [default-item-id]}]]
  (if-let [derived-item-id (r router/get-current-route-path-param db :item-id)]
          (let [derived-item-id (keyword derived-item-id)]
               (return derived-item-id))
          (return default-item-id)))

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

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]])

(a/reg-sub :item-browser/get-view-props get-view-props)
