
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.events
    (:require [app-plugins.item-lister.events]
              [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]
              [x.app-ui.api   :as ui]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-lister.events
(def set-error-mode!        app-plugins.item-lister.events/set-error-mode!)
(def use-filter!            app-plugins.item-lister.events/use-filter!)
(def load-lister!           app-plugins.item-lister.events/load-lister!)
(def disable-items!         app-plugins.item-lister.events/disable-items!)
(def enable-items!          app-plugins.item-lister.events/enable-items!)
(def toggle-item-selection! app-plugins.item-lister.events/toggle-item-selection!)



;; ----------------------------------------------------------------------------
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [item-dex (r subs/get-item-dex db extension-id item-namespace item-id)]
       (r disable-items! db extension-id item-namespace [item-dex])))

(defn enable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [item-dex (r subs/get-item-dex db extension-id item-namespace item-id)]
       (r enable-items! db extension-id item-namespace [item-dex])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r subs/get-item db extension-id item-namespace item-id)]
       (assoc-in db [extension-id :item-browser/backup-items item-id] backup-item)))

(defn apply-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id changes]]
  (let [item-dex (r subs/get-item-dex db extension-id item-namespace item-id)]
       (update-in db [extension-id :item-lister/data-items item-dex] merge changes)))

(defn revert-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r subs/get-backup-item db extension-id item-namespace item-id)
        item-dex    (r subs/get-item-dex    db extension-id item-namespace item-id)]
       (assoc-in db [extension-id :item-lister/data-items item-dex] backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (as-> db % (r backup-item!     % extension-id item-namespace item-id)
             (r disable-item!    % extension-id item-namespace item-id)
             (r ui/fake-process! % 15)))

(defn delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (r enable-item! db extension-id item-namespace item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id changes]]
  (as-> db % (r backup-item!   % extension-id item-namespace item-id)
             (r disable-item!  % extension-id item-namespace item-id)
             (r apply-changes! % extension-id item-namespace item-id changes)))

(defn item-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (r enable-item! db extension-id item-namespace item-id))

(defn update-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (as-> db % (r revert-changes! % extension-id item-namespace item-id)
             (r enable-item!    % extension-id item-namespace item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
