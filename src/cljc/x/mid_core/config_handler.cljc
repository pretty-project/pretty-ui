
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.18
; Description:
; Version: v0.6.6
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.config-handler
    (:require [x.mid-core.event-handler :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-details
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :app-details]))

(defn get-app-detail
  ; @param (keyword) config-item-id
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :app-details config-item-id]))

(defn get-browser-details
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :browser-details]))

(defn get-browser-detail
  ; @param (keyword) config-item-id
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :browser-details config-item-id]))

(defn get-database-details
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :database-details]))

(defn get-database-detail
  ; @param (keyword) config-item-id
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :database-details config-item-id]))

(defn get-site-links
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :site-links]))

(defn get-site-link
  ; @param (keyword) site-link-id
  ;
  ; @return (map)
  [db [_ site-link-id]]
  (get-in db [::configs :data-items :site-links site-link-id]))

(defn get-seo-details
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :seo-details]))

(defn get-seo-detail
  ; @param (keyword) config-item-id
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :seo-details config-item-id]))

(defn get-storage-details
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :storage-details]))

(defn get-storage-detail
  ; @param (keyword) config-item-id
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :storage-details config-item-id]))

(defn get-js-details
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :js-details]))

(defn get-js-detail
  ; @param (keyword) config-item-id
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :js-details config-item-id]))

(defn get-css-paths
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :css-paths]))

(defn get-favicon-paths
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :favicon-paths]))

(defn get-plugin-js-paths
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :plugin-js-paths]))

(defn get-configs
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items]))

(defn get-destructed-configs
  ; @return (map)
  [db _]
  (merge (r get-app-details     db)
         (r get-browser-details db)
         (r get-seo-details     db)
         (r get-storage-details db)
         (r get-js-details      db)
         {:css-paths       (r get-css-paths       db)
          :favicon-paths   (r get-favicon-paths   db)
          :plugin-js-paths (r get-plugin-js-paths db)}))

(defn get-config-item
  ; @param (keyword) config-item-id
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (let [destructed-configs (r get-destructed-configs db)]
       (get destructed-configs config-item-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-configs!
  ; @param (map) configs
  ;
  ; @return (map)
  [db [_ configs]]
  (assoc-in db [::configs :data-items] configs))
