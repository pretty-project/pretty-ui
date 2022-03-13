
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.events
    (:require [mid-fruits.map                  :refer [dissoc-in]]
              [plugins.item-browser.core.subs  :as core.subs]
              [plugins.item-lister.core.events :as plugins.item-lister.core.events]
              [x.app-core.api                  :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.events
(def set-error-mode! plugins.item-lister.core.events/set-error-mode!)
(def use-filter!     plugins.item-lister.core.events/use-filter!)
;(def load-lister!    plugins.item-lister.core.events/load-lister!)



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

(defn store-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:item-id (string)(opt)}}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]])
;  (if (r subs/route-handled?     db extension-id item-namespace)
;      (r derive-current-item-id! db extension-id item-namespace)
;      (if-let [item-id (get browser-props :item-id)]
;              (r set-current-item-id! db extension-id item-namespace item-id)
;              (r use-root-item-id!    db extension-id item-namespace)]])

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
             (r store-current-item-id! % extension-id item-namespace browser-props)))
             ;(r load-lister!           % extension-id item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
