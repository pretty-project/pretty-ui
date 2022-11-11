
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.backup.subs
    (:require [engines.engine-handler.backup.subs :as backup.subs]
              [engines.item-editor.body.subs      :as body.subs]
              [engines.item-editor.core.subs      :as core.subs]
              [engines.item-editor.download.subs  :as download.subs]
              [forms.api                          :as forms]
              [mid-fruits.candy                   :refer [return]]
              [mid-fruits.mixed                   :as mixed]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.backup.subs
(def get-backup-item    backup.subs/get-backup-item)
(def export-backup-item backup.subs/export-backup-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-changes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ editor-id item-id]]
  (get-in db [:engines :engine-handler/item-changes editor-id item-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; XXX#6000 (source-code/cljc/mid/forms/helpers.cljc)
  ; XXX#6001 (source-code/cljc/mid/forms/helpers.cljc)
  ; XXX#5671 (source-code/cljs/engines/item_editor/backup/subs.cljs)
  ; Az item-changed? függvény összehasonlítja az elem kezdeti értékéről készült
  ; másolatot az elem jelenlegi állapotával.
  (if-let [data-received? (r download.subs/data-received? db editor-id)]
          (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                current-item    (r core.subs/get-current-item    db editor-id)
                backup-item     (r get-backup-item               db editor-id current-item-id)]
               (forms/items-different? current-item backup-item))))

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
  ; A form-changed? függvény összehasonlítja az elem {:change-keys [...]} paraméterként
  ; átadott kulcsainak értékeit az elemről tárolt másolat azonos értékeivel.
  (if-let [data-received? (r download.subs/data-received? db editor-id)]
          (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                current-item    (r core.subs/get-current-item    db editor-id)
                backup-item     (r get-backup-item               db editor-id current-item-id)]
               (forms/items-different? current-item backup-item change-keys))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/item-changed? :my-editor]
(r/reg-sub :item-editor/item-changed? item-changed?)

; @param (keyword) editor-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:item-editor/form-changed? :my-editor [:name :email-address]]
(r/reg-sub :item-editor/form-changed? form-changed?)
