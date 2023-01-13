
(ns templates.item-lister.update.views
    (:require [components.api :as components]))

;; -- Reorder items components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-reordered-dialog-body
  ; @param (keyword) lister-id
  [lister-id]
  [components/notification-bubble :item-lister/items-reordered-dialog
                                  {:label :changes-saved}])

(defn reorder-items-failed-dialog-body
  ; @param (keyword) lister-id
  [lister-id]
  [components/notification-bubble :item-lister/reorder-items-failed-dialog
                                  {:label :failed-to-save-changes}])

;; -- Delete items components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-deleted-dialog-body
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  [lister-id item-ids]
  (let [on-failure [:item-lister/undo-delete-items-failed lister-id item-ids]
        on-success [:item-lister/delete-items-undid       lister-id]
        undo-event [:item-lister/undo-delete-items!       lister-id item-ids
                                                          {:on-failure on-failure :on-success on-success}]]
       [components/notification-bubble :item-lister/items-deleted-dialog
                                       {:label          {:content :n-items-deleted :replacements [(count item-ids)]}
                                        :primary-button {:label :recover! :on-click undo-event}}]))

;; -- Duplicate items components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-duplicated-dialog-body
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  [lister-id copy-ids]
  ; XXX#7002 (source-code/cljs/templates/item_lister/update/README.md)
  [components/notification-bubble :item-lister/items-duplicated-dialog
                                  {:label {:content :n-items-duplicated :replacements [(count copy-ids)]}}])
