
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.events
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.map                    :refer [dissoc-in]]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-ui.api                      :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (assoc-in db [:plugins :item-editor/meta-items extension-id :error-mode?] true))

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  ; A {:recovery-mode? true} beállítással elindítitott szerkesztő visszaállítja az elem eltárolt változtatásait
  (assoc-in db [:plugins :item-editor/meta-items extension-id :recovery-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (dissoc-in db [:plugins :item-editor/meta-items extension-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [derived-item-id (r core.subs/get-derived-item-id db extension-id item-namespace)]
       (assoc-in db [:plugins :item-editor/meta-items extension-id :item-id] derived-item-id)))

(defn set-route-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
       (r ui/set-header-title! db route-title)))

(defn set-auto-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [auto-title (r core.subs/get-auto-title db extension-id item-namespace)]
       (r ui/set-header-title! db auto-title)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
       (cond-> db :store-derived-view-id! (as-> % (r store-derived-view-id! % extension-id item-namespace))
                  route-title             (as-> % (r set-route-title!       % extension-id item-namespace)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/set-error-mode! set-error-mode!)
