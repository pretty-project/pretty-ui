
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



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn app-detail-path
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (a/app-detail-path :my-config-item)
  ;
  ; @return (item-path vector)
  [config-item-id]
  [::configs :data-items :app-details config-item-id])

(defn browser-detail-path
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (a/browser-detail-path :my-config-item)
  ;
  ; @return (item-path vector)
  [config-item-id]
  [::configs :data-items :browser-details config-item-id])

(defn database-detail-path
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (a/database-detail-path :my-config-item)
  ;
  ; @return (item-path vector)
  [config-item-id]
  [::configs :data-items :database-details config-item-id])

(defn install-detail-path
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (a/install-detail-path :my-config-item)
  ;
  ; @return (item-path vector)
  [config-item-id]
  [::configs :data-items :install-details config-item-id])

(defn seo-detail-path
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (a/seo-detail-path :my-config-item)
  ;
  ; @return (item-path vector)
  [config-item-id]
  [::configs :data-items :seo-details config-item-id])

(defn storage-detail-path
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (a/storage-detail-path :my-config-item)
  ;
  ; @return (item-path vector)
  [config-item-id]
  [::configs :data-items :storage-details config-item-id])

(defn js-detail-path
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (a/js-detail-path :my-config-item)
  ;
  ; @return (item-path vector)
  [config-item-id]
  [::configs :data-items :js-details config-item-id])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-details
  ; @usage
  ;  (r a/get-app-details db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :app-details]))

(defn get-app-detail
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (r a/get-app-detail db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :app-details config-item-id]))

(defn get-browser-details
  ; @usage
  ;  (r a/get-browser-details db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :browser-details]))

(defn get-browser-detail
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (r a/get-browser-detail db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :browser-details config-item-id]))

(defn get-database-details
  ; @usage
  ;  (r a/get-database-details db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :database-details]))

(defn get-database-detail
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (r a/get-database-detail db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :database-details config-item-id]))

(defn get-install-details
  ; @usage
  ;  (r a/get-install-details db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :install-details]))

(defn get-install-detail
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (r a/get-install-detail db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :install-details config-item-id]))

(defn get-seo-details
  ; @usage
  ;  (r a/get-seo-details db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :seo-details]))

(defn get-seo-detail
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (r a/get-seo-detail db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :seo-details config-item-id]))

(defn get-storage-details
  ; @usage
  ;  (r a/get-storage-details db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :storage-details]))

(defn get-storage-detail
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (r a/get-storage-detail db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :storage-details config-item-id]))

(defn get-js-details
  ; @usage
  ;  (r a/get-js-details db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :js-details]))

(defn get-js-detail
  ; @param (keyword) config-item-id
  ;
  ; @usage
  ;  (r a/get-js-detail db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-id]]
  (get-in db [::configs :data-items :js-details config-item-id]))

(defn get-css-paths
  ; @usage
  ;  (r a/get-css-paths db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :css-paths]))

(defn get-favicon-paths
  ; @usage
  ;  (r a/get-favicon-paths db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :favicon-paths]))

(defn get-plugin-js-paths
  ; @usage
  ;  (r a/get-plugin-js-paths db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items :plugin-js-paths]))

(defn get-configs
  ; @usage
  ;  (r a/get-configs db)
  ;
  ; @return (map)
  [db _]
  (get-in db [::configs :data-items]))

(defn get-destructed-configs
  ; @usage
  ;  (r a/get-destructed-configs db)
  ;
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
  ; @usage
  ;  (r a/get-config-item db :my-config-item)
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
