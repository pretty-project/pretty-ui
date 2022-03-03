
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.subs
    (:require [app-plugins.item-lister.subs]
              [app-plugins.item-browser.engine :as engine]
              [mid-fruits.candy                :refer [param return]]
              [mid-fruits.keyword              :as keyword]
              [mid-fruits.loop                 :refer [some-indexed]]
              [mid-plugins.item-browser.subs   :as subs]
              [x.app-core.api                  :as a :refer [r]]
              [x.app-db.api                    :as db]
              [x.app-router.api                :as router]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-lister.subs
(def get-downloaded-items   app-plugins.item-lister.subs/get-downloaded-items)
(def get-description        app-plugins.item-lister.subs/get-description)
(def lister-disabled?       app-plugins.item-lister.subs/lister-disabled?)
(def toggle-item-selection? app-plugins.item-lister.subs/toggle-item-selection?)

; mid-plugins.item-browser.subs
(def get-browser-props subs/get-browser-props)
(def get-meta-item     subs/get-meta-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-item-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-browser/get-current-item-id db :my-extension :my-type)
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :item-id))

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r router/get-current-route-path-param db :item-id))

(defn get-root-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :root-item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)
        label-key       (r get-meta-item       db extension-id item-namespace :label-key)]
       (get-in db [extension-id :item-browser/data-items current-item-id label-key])))

(defn get-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (maps in vector)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)
        path-key        (r get-meta-item       db extension-id item-namespace :path-key)
        item-path       (get-in db [extension-id :item-browser/data-items current-item-id path-key])]
       (vec item-path)))

(defn get-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (letfn [(f [{:keys [id] :as item}] (if (= id item-id) item))]
         (let [downloaded-items (r get-downloaded-items db extension-id item-namespace)]
              (some f downloaded-items))))

(defn get-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace item-id]]
  (letfn [(f [item-dex {:keys [id]}] (if (= id item-id) item-dex))]
         (let [downloaded-items (r get-downloaded-items db extension-id item-namespace)]
              (some-indexed f downloaded-items))))

(defn export-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace item-id]]
  (let [item (r get-item db extension-id item-namespace item-id)]
       (db/document->namespaced-document item item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r lister-disabled? db extension-id item-namespace))

(defn at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [item-path (r get-item-path db extension-id item-namespace)]
       (empty? item-path)))

(defn get-parent-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (get-in db [extension-id :item-browser/backup-items item-id]))

(defn export-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r get-backup-item db extension-id item-namespace item-id)]
       (db/document->namespaced-document backup-item item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/get-item-label :my-extension :my-type]
(a/reg-sub :item-browser/get-item-label get-item-label)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/browser-disabled? :my-extension :my-type]
(a/reg-sub :item-browser/browser-disabled? browser-disabled?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/at-home? :my-extension :my-type]
(a/reg-sub :item-browser/at-home? at-home?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/get-description :my-extension :my-type]
(a/reg-sub :item-browser/get-description get-description)
