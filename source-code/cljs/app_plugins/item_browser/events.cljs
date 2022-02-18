
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.5.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.events
    (:require [app-plugins.item-lister.events]
              [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-lister.events
(def set-error-mode! app-plugins.item-lister.events/set-error-mode!)
(def use-filter!     app-plugins.item-lister.events/use-filter!)
(def load-lister!    app-plugins.item-lister.events/load-lister!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-lister/meta-items :error-mode?]))

(defn derive-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (assoc-in db [extension-id :item-browser/meta-items :item-id]
               (or (r subs/get-derived-item-id db extension-id item-namespace)
                   (r subs/get-root-item-id    db extension-id item-namespace))))

(defn use-root-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (assoc-in db [extension-id :item-browser/meta-items :item-id]
               (r subs/get-root-item-id db extension-id item-namespace)))

(defn set-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (assoc-in db [extension-id :item-browser/meta-items :item-id] item-id))

(defn store-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:item-id (string)(opt)}}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (if (r subs/route-handled?     db extension-id item-namespace)
      (r derive-current-item-id! db extension-id item-namespace)
      (if-let [item-id (get browser-props :item-id)]
              (r set-current-item-id! db extension-id item-namespace item-id)
              (r use-root-item-id!    db extension-id item-namespace))))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (engine/resolver-id extension-id item-namespace :get)
        document    (get server-response resolver-id)]
       ; XXX#3907
       ; Az item-lister pluginnal megegyezően az item-browser plugin is névtér nélkül tárolja
       ; a letöltött dokumentumot
       (let [document    (db/document->non-namespaced-document document)
             document-id (get document :id)]
            (assoc-in db [extension-id :item-browser/data-items document-id] document))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (r store-downloaded-item! db extension-id item-namespace server-response))

(defn load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (as-> db % (r reset-browser!         % extension-id item-namespace)
             (r store-current-item-id! % extension-id item-namespace browser-props)
             (r load-lister!           % extension-id item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
