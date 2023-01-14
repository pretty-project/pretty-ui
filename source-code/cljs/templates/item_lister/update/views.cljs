
(ns templates.item-lister.update.views
    (:require [components.api :as components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-reordered-dialog
  ; @param (keyword) lister-id
  [_]
  [components/notification-bubble :item-lister/items-reordered-dialog
                                  {:content :changes-saved}])

(defn reorder-items-failed-dialog
  ; @param (keyword) lister-id
  [_]
  [components/notification-bubble :item-lister/reorder-items-failed-dialog
                                  {:content :failed-to-save-changes}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-failed-dialog
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  [_ _]
  [components/notification-bubble :item-lister/delete-items-failed-dialog
                                  {:content :failed-to-delete}])

(defn items-deleted-dialog
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  [lister-id item-ids]
  (let [on-failure  [:item-lister/undo-delete-items-failed lister-id item-ids]
        on-success  [:item-lister/delete-items-undid       lister-id]
        undo-event  [:item-lister/undo-delete-items!       lister-id item-ids
                                                           {:on-failure on-failure :on-success on-success}]
        undo-button {:color :primary :label :recover! :layout :button :on-click undo-event}]
       [components/notification-bubble :item-lister/items-deleted-dialog
                                       {:content {:content :n-items-deleted :replacements [(count item-ids)]}
                                        :secondary-button undo-button}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-items-failed-dialog
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  [_ _]
  [components/notification-bubble :item-lister/undo-delete-items-failed-dialog
                                  {:content :failed-to-undo-delete}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-duplicated-dialog
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  [_ copy-ids]
  ; XXX#7002 (source-code/cljs/templates/item_lister/update/README.md)
  [components/notification-bubble :item-lister/items-duplicated-dialog
                                  {:content {:content :n-items-duplicated :replacements [(count copy-ids)]}}])

(defn duplicate-items-failed-dialog
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  [_ copy-ids]
  [components/notification-bubble :item-lister/duplicate-items-failed-dialog
                                  {:content :failed-to-duplicate}])
