
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.backup.events
    (:require [engines.file-editor.backup.subs :as backup.subs]
              [engines.file-editor.body.subs   :as body.subs]
              [engines.file-editor.core.subs   :as core.subs]
              [re-frame.api                    :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-current-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [current-content (r core.subs/get-current-content db editor-id)]
       (assoc-in db [:engines :engine-handler/backup-items editor-id] current-content)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn revert-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A revert-item! függvény visszaállítja az aktuálisan szerkesztett tartalmat
  ; a megnyitáskori állapotára a tartalom letöltésekor eltárolt másolat alapján.
  (let [content-path   (r body.subs/get-body-prop        db editor-id :content-path)
        backup-content (r backup.subs/get-backup-content db editor-id)]
       (assoc-in db content-path backup-content)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :file-editor/revert-content! revert-content!)
