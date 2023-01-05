
(ns templates.item-handler.update.views
    (:require [x.ui.api :as x.ui]))

;; -- Delete item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  [handler-id item-id action-props]
  (let [on-failure  [:item-handler/undo-delete-item-failed handler-id item-id action-props]
        on-success  [:item-handler/delete-item-undid       handler-id         action-props]
        undo-event  [:item-handler/undo-delete-item!       handler-id item-id
                                                           {:on-failure on-failure :on-success on-success
                                                            :display-progress? true :progress-max 50}]
        close-event [:x.ui/remove-bubble! :item-handler/item-deleted-dialog]]
       [x.ui/state-changed-bubble-body :item-handler/item-deleted-dialog
                                       {:label          :item-deleted
                                        :primary-button {:label :recover! :on-click {:dispatch-n [close-event undo-event]}}}]))

;; -- Undo delete item components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-failed-dialog-body
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  [handler-id item-id action-props]
  (let [on-failure  [:item-handler/undo-delete-item-failed handler-id item-id action-props]
        on-success  [:item-handler/delete-item-undid       handler-id         action-props]
        retry-event [:item-handler/undo-delete-item!       handler-id item-id
                                                           {:on-failure on-failure :on-success on-success
                                                            :display-progress? true :progress-max 50}]
        close-event [:x.ui/remove-bubble! :item-handler/undo-delete-item-failed-dialog]]
       ; BUG#6170 (source-code/cljs/templates/item_browser/update/views.cljs)
       [x.ui/state-changed-bubble-body :item-handler/undo-delete-item-failed-dialog
                                       {:label          :failed-to-undo-delete
                                        :primary-button {:label :retry! :on-click retry-event}}]))

;; -- Duplicate item components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-duplicated-dialog-body
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  ; @param (map) action-props
  [handler-id copy-id action-props]
  (let [view-event [:item-handler/view-duplicated-item! handler-id copy-id action-props]]
       [x.ui/state-changed-bubble-body :item-handler/item-duplicated-dialog
                                       {:label          :item-duplicated
                                        :primary-button {:label :view-copy! :on-click view-event}}]))
