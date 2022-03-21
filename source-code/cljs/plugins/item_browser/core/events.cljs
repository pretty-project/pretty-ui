
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.events
    (:require [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [plugins.item-lister.core.events    :as plugins.item-lister.core.events]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-ui.api                       :as ui]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.events
(def set-error-mode! plugins.item-lister.core.events/set-error-mode!)
(def use-filter!     plugins.item-lister.core.events/use-filter!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-browser/meta-items]))



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
               (or (r core.subs/get-derived-item-id db extension-id item-namespace)
                   (r core.subs/get-root-item-id    db extension-id item-namespace))))

(defn use-root-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (assoc-in db [extension-id :item-browser/meta-items :item-id]
               (r core.subs/get-root-item-id db extension-id item-namespace)))

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

(defn store-derived-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [derived-item-id (r core.subs/get-derived-item-id db extension-id item-namespace)]
       (assoc-in db [:plugins :item-lister/meta-items extension-id :item-id] derived-item-id)))

;  (if (r subs/route-handled?     db extension-id item-namespace)
;      (r derive-current-item-id! db extension-id item-namespace)
;      (if-let [item-id (get browser-props :item-id)]
;              (r set-current-item-id! db extension-id item-namespace item-id)
;              (r use-root-item-id!    db extension-id item-namespace)]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (let [route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
       (cond-> db :store-derived-item-id (as-> % (r store-derived-item-id! % extension-id item-namespace))
                  route-title            (as-> % (r ui/set-header-title!   % route-title)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
