
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.backup.subs
    (:require [engines.engine-handler.backup.subs :as backup.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.download.subs :as download.subs]
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
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ handler-id item-id]]
  (get-in db [:engines :engine-handler/item-changes handler-id item-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  ; XXX#6000 (mid.forms.helpers)
  ; XXX#6001 (mid.forms.helpers)
  ; XXX#5671 (engines.item-handler.backup.subs)
  ; Az item-changed? függvény összehasonlítja az elem kezdeti értékéről készült
  ; másolatot az elem jelenlegi állapotával.
  (if-let [data-received? (r download.subs/data-received? db handler-id)]
          (let [current-item-id (r core.subs/get-current-item-id db handler-id)
                current-item    (r core.subs/get-current-item    db handler-id)
                backup-item     (r get-backup-item               db handler-id current-item-id)]
               (forms/items-different? current-item backup-item))))

(defn form-changed?
  ; @param (keyword) handler-id
  ; @param (keywords in vector) change-keys
  ;
  ; @usage
  ;  (r form-changed? db :my-handler [:name :email-address])
  ;
  ; @return (boolean)
  [db [_ handler-id change-keys]]
  ; XXX#6000 (mid.forms.helpers)
  ; XXX#6001 (mid.forms.helpers)
  ; XXX#5672 (engines.item-handler.backup.subs)
  ; A form-changed? függvény összehasonlítja az elem {:change-keys [...]} paraméterként
  ; átadott kulcsainak értékeit az elemről tárolt másolat azonos értékeivel.
  (if-let [data-received? (r download.subs/data-received? db handler-id)]
          (let [current-item-id (r core.subs/get-current-item-id db handler-id)
                current-item    (r core.subs/get-current-item    db handler-id)
                backup-item     (r get-backup-item               db handler-id current-item-id)]
               (forms/items-different? current-item backup-item change-keys))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/item-changed? :my-handler]
(r/reg-sub :item-handler/item-changed? item-changed?)

; @param (keyword) handler-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:item-handler/form-changed? :my-handler [:name :email-address]]
(r/reg-sub :item-handler/form-changed? form-changed?)
