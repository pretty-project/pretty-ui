
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.item-editor.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-item-id!       core.events/set-item-id!)
(def set-view-id!       core.events/set-view-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items editor-id :error-mode?] true))

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A {:recovery-mode? true} beállítással elindítitott szerkesztő visszaállítja az elem eltárolt változtatásait
  (assoc-in db [:plugins :plugin-handler/meta-items editor-id :recovery-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [item-path        (r body.subs/get-body-prop db editor-id :item-path)
        suggestions-path (r body.subs/get-body-prop db editor-id :suggestions-path)]
       (-> db (dissoc-in [:plugins :plugin-handler/meta-items editor-id :data-received?])
              (dissoc-in item-path)
              (dissoc-in suggestions-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ editor-id item-id]])
  ;(as-> db % (r set-item-id! db editor-id (or item-id (r core.subs/read-item-id db editor-id)))
  ;           (r set-view-id! db editor-id (r core.subs/read-view-id db editor-id))]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/set-error-mode! set-error-mode!)
