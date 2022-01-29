
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.subs
    (:require [app-plugins.item-lister.subs]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a :refer [r]]
              [x.app-router.api   :as router]
              [app-plugins.item-browser.engine :as engine]
              [mid-plugins.item-browser.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.subs
(def get-browser-props subs/get-browser-props)
(def get-meta-item     subs/get-meta-item)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-root-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :root-item-id))

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r router/get-current-route-path-param db :item-id))

(defn get-current-item-id
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r item-browser/get-current-item-id db :my-extension)
  ;
  ; @return (string)
  [db [_ extension-id]]
  (get-in db [extension-id :item-browser/meta-items :item-id]))

(defn get-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id)
        label-key       (r get-meta-item       db extension-id item-namespace :label-key)]
       (get-in db [extension-id :item-browser/data-items current-item-id label-key])))

(a/reg-sub :item-browser/get-item-label get-item-label)

(defn get-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (maps in vector)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id)
        path-key        (r get-meta-item       db extension-id item-namespace :path-key)
        item-path       (get-in db [extension-id :item-browser/data-items current-item-id path-key])]
       (vec item-path)))

(defn at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [item-path (r get-item-path db extension-id item-namespace)]
       (empty? item-path)))

(defn get-parent-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [item-path (r get-item-path db extension-id item-namespace)]
       (if-let [parent-link (last item-path)]
               (get parent-link (keyword/add-namespace item-namespace :id)))))

(defn route-handled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [route-id (r router/get-current-route-id db)]
       (or (= route-id (engine/route-id          extension-id item-namespace))
           (= route-id (engine/extended-route-id extension-id item-namespace)))))

(defn set-title?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r route-handled? db extension-id item-namespace))

(defn get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:at-home? (boolean)
  ;   :error-mode? (boolean)
  ;   :item-path (maps in vector)}
  [db [_ extension-id item-namespace]]
  {:at-home?    (r at-home?      db extension-id item-namespace)
   :item-path   (r get-item-path db extension-id item-namespace)
   :error-mode? (r app-plugins.item-lister.subs/get-meta-item db extension-id item-namespace :error-mode?)})

(a/reg-sub :item-browser/get-header-props get-header-props)

(defn get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:at-home? (boolean)
  ;   :item-path (maps in vector)}
  [db [_ extension-id item-namespace]])
  ;(r item-lister/get-body-props extension-id item-namespace))

(a/reg-sub :item-browser/get-body-props get-header-props)

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (if-let [error-mode? (r get-meta-item db extension-id item-namespace :error-mode?)]
          {:error-mode? true}
          {:description (r app-plugins.item-lister.subs/get-view-props db extension-id item-namespace)}))

(a/reg-sub :item-browser/get-view-props get-view-props)
