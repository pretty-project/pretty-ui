
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.update.views
    (:require [x.ui.api :as x.ui]))



;; -- Reorder items components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-reordered-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [x.ui/state-changed-bubble-body :engines.item-lister/items-reordered-dialog
                                  {:label :changes-saved}])

(defn reorder-items-failed-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [x.ui/state-changed-bubble-body :engines.item-lister/reorder-items-failed-dialog
                                  {:label :failed-to-save-changes}])



;; -- Delete items components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  [lister-id item-ids]
  (let [undo-event [:item-lister/undo-delete-items! lister-id item-ids]]
       [x.ui/state-changed-bubble-body :engines.item-lister/items-deleted-dialog
                                       {:label          {:content :n-items-deleted :replacements [(count item-ids)]}
                                        :primary-button {:label :recover! :on-click undo-event}}]))



;; -- Duplicate items components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  [lister-id copy-ids]
  ; XXX#7002
  [x.ui/state-changed-bubble-body :engines.item-lister/items-duplicated-dialog
                                  {:label {:content :n-items-duplicated :replacements [(count copy-ids)]}}])
