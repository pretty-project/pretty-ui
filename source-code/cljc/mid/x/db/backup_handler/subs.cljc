
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.db.backup-handler.subs
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r get-backup-item db [:my-item])
  ;
  ; @return (*)
  [db [_ item-path]]
  (get-in db [:db :backup-handler/backup-items item-path]))

(defn item-changed?
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r item-changed? db [:my-item])
  ;
  ; @return (boolean)
  [db [_ item-path]]
  (not= (get-in db item-path)
        (r get-backup-item db item-path)))

(defn item-unchanged?
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r item-unchanged? db [:my-item])
  ;
  ; @return (boolean)
  [db [_ item-path]]
  (let [item-changed? (r item-changed? db item-path)]
       (not item-changed?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.db/get-backup-item [:my-item]]
(r/reg-sub :x.db/get-backup-item get-backup-item)

; @usage
;  [:x.db/item-changed? [:my-item]]
(r/reg-sub :x.db/item-changed? item-changed?)

; @usage
;  [:x.db/item-unchanged? [:my-item]]
(r/reg-sub :x.db/item-unchanged? item-unchanged?)
