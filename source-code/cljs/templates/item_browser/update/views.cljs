
(ns templates.item-browser.update.views
    (:require [components.api :as components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-failed-dialog
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [_ _]
  [components/notification-bubble :item-browser/update-item-failed-dialog
                                  {:content :failed-to-update}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [browser-id item-id]
  (let [on-failure [:item-browser/undo-delete-item-failed browser-id item-id]
        on-success [:item-browser/delete-item-undid       browser-id]
        undo-event [:item-browser/undo-delete-item!       browser-id item-id
                                                          {:on-failure on-failure :on-success on-success
                                                           :display-progress? true :progress-max 50}]
        close-event [:x.ui/remove-bubble! :item-browser/item-deleted-dialog]
        undo-button {:color :primary :layout :button :label :recover! :on-click {:dispatch-n [close-event undo-event]}}]
       [components/notification-bubble :item-browser/item-deleted-dialog
                                       {:content :item-deleted :secondary-button undo-button}]))

(defn delete-item-failed-dialog
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [_ _]
  [components/notification-bubble :item-browser/delete-item-failed-dialog
                                  {:content :failed-to-delete}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-failed-dialog
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [browser-id item-id]
  (let [on-failure   [:item-browser/undo-delete-item-failed browser-id item-id]
        on-success   [:item-browser/delete-item-undid       browser-id]
        retry-event  [:item-browser/undo-delete-item!       browser-id item-id
                                                            {:on-failure on-failure :on-success on-success
                                                             :display-progress? true :progress-max 50}]
        close-event  [:x.ui/remove-bubble! :item-browser/undo-delete-item-failed-dialog]
        retry-button {:color :primary :layout :button :label :retry! :on-click retry-event}]
       ; BUG#6170
       ; Why the close event doesn't called in the on-click method?
       ; This dialog appears every time after when the user clicks on the 'Retry'
       ; button and the recovering repedeatly fails. Therefore in case of the close
       ; event fired when the user clicks on the button, the notification renderer
       ; might closes the notification just after it reappeared!
       [components/notification-bubble :item-browser/undo-delete-item-failed-dialog
                                       {:content :failed-to-undo-delete :secondary-button retry-button}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-duplicated-dialog
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  [_ _]
  ; XXX#7002 (source-code/cljs/templates/item_lister/update/README.md)
  [components/notification-bubble :item-browser/item-duplicated-dialog
                                  {:content :item-duplicated}])

(defn duplicate-item-failed-dialog
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  [_ _]
  [components/notification-bubble :item-browser/duplicate-item-failed-dialog
                                  {:content :failed-to-duplicate}])
