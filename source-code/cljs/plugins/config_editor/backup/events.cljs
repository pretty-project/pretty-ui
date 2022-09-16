
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.backup.events
    (:require [plugins.config-editor.backup.subs :as backup.subs]
              [plugins.config-editor.body.subs   :as body.subs]
              [plugins.config-editor.core.subs   :as core.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-current-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [current-config (r core.subs/get-current-config db editor-id)]
       (assoc-in db [:plugins :plugin-handler/backup-items editor-id] current-config)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn revert-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A revert-item! függvény visszaállítja az aktuálisan szerkesztett tartalmat
  ;  a megnyitáskori állapotára a tartalom letöltésekor eltárolt másolat alapján.
  (let [config-path   (r body.subs/get-body-prop       db editor-id :config-path)
        backup-config (r backup.subs/get-backup-config db editor-id)]
       (assoc-in db config-path backup-config)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :config-editor/revert-config! revert-config!)
