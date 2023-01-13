
(ns templates.item-browser.update.views
    (:require [components.api :as components]))

;; -- Delete item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [browser-id item-id]
  (let [on-failure [:item-browser/undo-delete-item-failed browser-id item-id]
        on-success [:item-browser/delete-item-undid       browser-id]
        undo-event [:item-browser/undo-delete-item!       browser-id item-id
                                                          {:on-failure on-failure :on-success on-success
                                                           :display-progress? true :progress-max 50}]
        close-event [:x.ui/remove-bubble! :item-browser/item-deleted-dialog]]
       [components/notification-bubble :item-browser/item-deleted-dialog
                                       {:label          :item-deleted
                                        :primary-button {:label :recover! :on-click {:dispatch-n [close-event undo-event]}}}]))

;; -- Undo delete item components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-failed-dialog-body
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [browser-id item-id]
  (let [on-failure  [:item-browser/undo-delete-item-failed browser-id item-id]
        on-success  [:item-browser/delete-item-undid       browser-id]
        retry-event [:item-browser/undo-delete-item!       browser-id item-id
                                                           {:on-failure on-failure :on-success on-success
                                                            :display-progress? true :progress-max 50}]
        close-event [:x.ui/remove-bubble! :item-browser/undo-delete-item-failed-dialog]]
       ; BUG#6170
       ; Why the close event doesn't called in the on-click method?
       ; This dialog appears every time after the user clicks on the 'Retry' button
       ; and the recovering repedeatly fails. Therefore if the close event fires when
       ; the user clicks on the button, the notification renderer might closes
       ; the notification after it reappeared!
       [components/notification-bubble :item-browser/undo-delete-item-failed-dialog
                                       {:label          :failed-to-undo-delete
                                        :primary-button {:label :retry! :on-click retry-event}}]))

;; -- Duplicate item components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-duplicated-dialog-body
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  [browser-id _]
  ; XXX#7002 (source-code/cljs/templates/item_lister/update/README.md)
  [components/notification-bubble :item-browser/item-duplicated-dialog
                                  {:label :item-duplicated}])
