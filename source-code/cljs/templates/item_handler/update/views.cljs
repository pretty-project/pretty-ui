
(ns templates.item-handler.update.views
    (:require [components.api :as components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-saved-dialog
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  [_ _ _]
  [components/notification-bubble :item-handler/item-saved-dialog
                                  {:content :changes-saved}])

(defn save-item-failed-dialog
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  [_ _ _]
  [components/notification-bubble :item-handler/save-item-failed
                                  {:content :failed-to-save}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  [handler-id item-id action-props]
  (let [on-failure  [:item-handler/undo-delete-item-failed handler-id item-id action-props]
        on-success  [:item-handler/delete-item-undid       handler-id         action-props]
        undo-event  [:item-handler/undo-delete-item!       handler-id item-id
                                                           {:on-failure on-failure :on-success on-success
                                                            :display-progress? true :progress-max 50}]
        close-event [:x.ui/remove-bubble! :item-handler/item-deleted-dialog]
        undo-button {:color :primary :label :recover! :layout :button :on-click {:dispatch-n [close-event undo-event]}}]
       [components/notification-bubble :item-handler/item-deleted-dialog
                                       {:content :item-deleted :secondary-button undo-button}]))

(defn delete-item-failed-dialog
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  [_ _ _]
  [components/notification-bubble :item-handler/delete-item-failed
                                  {:content :failed-to-delete}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-failed-dialog
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  [handler-id item-id action-props]
  (let [on-failure   [:item-handler/undo-delete-item-failed handler-id item-id action-props]
        on-success   [:item-handler/delete-item-undid       handler-id         action-props]
        retry-event  [:item-handler/undo-delete-item!       handler-id item-id
                                                            {:on-failure on-failure :on-success on-success
                                                             :display-progress? true :progress-max 50}]
        close-event  [:x.ui/remove-bubble! :item-handler/undo-delete-item-failed-dialog]
        retry-button {:color :primary :label :retry! :layout :button :on-click retry-event}]
       ; BUG#6170 (source-code/cljs/templates/item_browser/update/views.cljs)
       [components/notification-bubble :item-handler/undo-delete-item-failed-dialog
                                       {:content :failed-to-undo-delete :secondary-button retry-button}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-item-failed-dialog
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  ; @param (map) action-props
  [_ _ _]
  [components/notification-bubble :item-handler/duplicate-item-failed
                                  {:content :failed-to-duplicate}])

(defn item-duplicated-dialog
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  ; @param (map) action-props
  [handler-id copy-id action-props]
  (let [view-event  [:item-handler/view-duplicated-item! handler-id copy-id action-props]
        view-button {:color :primary :layout :button :label :view-copy! :on-click view-event}]
       [components/notification-bubble :item-handler/item-duplicated-dialog
                                       {:content :item-duplicated :secondary-button view-button}]))
