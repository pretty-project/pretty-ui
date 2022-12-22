
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.backup.subs
    (:require [engines.engine-handler.backup.subs :as backup.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.core.subs     :as core.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.backup.subs
(def get-backup-item    backup.subs/get-backup-item)
(def export-backup-item backup.subs/export-backup-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn current-item-changed?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ; (r current-item-changed? db :my-handler)
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [current-item-id (r core.subs/get-current-item-id db handler-id)
        items-path      (r body.subs/get-body-prop       db handler-id :items-path)]
       (get-in db (conj items-path current-item-id :meta-items :changed?))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-handler/current-item-changed? :my-handler]
(r/reg-sub :item-handler/current-item-changed? current-item-changed?)
