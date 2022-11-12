
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.backup.subs
    (:require [candy.api                         :refer [return]]
              [engines.file-editor.body.subs     :as body.subs]
              [engines.file-editor.core.subs     :as core.subs]
              [engines.file-editor.download.subs :as download.subs]
              [forms.api                         :as forms]
              [mid-fruits.mixed                  :as mixed]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (get-in db [:engines :engine-handler/backup-items editor-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; XXX#6000 (source-code/cljc/mid/forms/helpers.cljc)
  ; XXX#6001 (source-code/cljc/mid/forms/helpers.cljc)
  ; XXX#5671 (source-code/cljs/engines/item_editor/backup/subs.cljs)
  (if-let [data-received? (r download.subs/data-received? db editor-id)]
          (let [current-content (r core.subs/get-current-content db editor-id)
                backup-content  (r get-backup-content            db editor-id)]
               (forms/items-different? current-content backup-content))))

(defn form-changed?
  ; @param (keyword) editor-id
  ; @param (keywords in vector) change-keys
  ;
  ; @usage
  ;  (r form-changed? db :my-editor [:name :email-address])
  ;
  ; @return (boolean)
  [db [_ editor-id change-keys]]
  ; XXX#6000 (source-code/cljc/mid/forms/helpers.cljc)
  ; XXX#6001 (source-code/cljc/mid/forms/helpers.cljc)
  ; XXX#5672 (source-code/cljs/engines/item_editor/backup/subs.cljs)
  (if-let [data-received? (r download.subs/data-received? db editor-id)]
          (let [current-content (r core.subs/get-current-content db editor-id)
                backup-content  (r get-backup-content            db editor-id)]
               (forms/items-different? current-content backup-content change-keys))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:file-editor/content-changed? :my-editor]
(r/reg-sub :file-editor/content-changed? content-changed?)

; @param (keyword) editor-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:file-editor/form-changed? :my-editor [:name :email-address]]
(r/reg-sub :file-editor/form-changed? form-changed?)
