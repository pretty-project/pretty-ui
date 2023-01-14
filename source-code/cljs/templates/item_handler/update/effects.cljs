
(ns templates.item-handler.update.effects
    (:require [engines.item-handler.api            :as item-handler]
              [re-frame.api                        :as r :refer [r]]
              [templates.item-handler.update.subs  :as update.subs]
              [templates.item-handler.update.views :as update.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/item-saved
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id action-props server-response]]
      (let [item-id (r item-handler/get-saved-item-id db handler-id server-response)]
           {:db       (r item-handler/unmark-item-as-changed! db handler-id item-id)
            :dispatch [:item-handler/render-item-saved-dialog! handler-id item-id action-props]})))

(r/reg-event-fx :item-handler/save-item-failed
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; @param (map) server-response
  (fn [_ [_ handler-id action-props _]]
      {:dispatch-n [[:item-handler/render-save-item-failed-dialog! handler-id nil action-props]
                    [:x.ui/end-listening-to-process!]]}))

(r/reg-event-fx :item-handler/render-item-saved-dialog!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  (fn [_ [_ handler-id item-id action-props]]
      [:x.ui/render-bubble! :item-handler/item-saved-dialog
                            {:content [update.views/item-saved-dialog handler-id item-id action-props]}]))

(r/reg-event-fx :item-handler/render-save-item-failed-dialog!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  (fn [_ [_ handler-id item-id action-props]]
      [:x.ui/render-bubble! :item-handler/save-item-failed-dialog
                            {:content [update.views/save-item-failed-dialog handler-id item-id action-props]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/item-deleted
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; {:base-route (string)
  ;  :lister-id (keyword)}
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id {:keys [base-route lister-id] :as action-props} server-response]]
      ; If the deleted item doesn't currently loaded in the item-handler ...
      ; ... reloads the related item-lister.
      ;
      ; If the deleted item currently loaded in the item-handler and other items
      ; downloaded in the related item-lister ...
      ; ... reloads the related item-lister, then jumps to the next item.
      ;
      ; If the deleted item loaded in the item-handler and NO other items downloaded
      ; in the related item-lister ...
      ; ... reloads the related item-lister, then jumps to the base route
      ;     (the item list will be empty after reloading).
      (let [item-id (r item-handler/get-deleted-item-id db handler-id server-response)]
           {:dispatch-n [[:item-handler/render-item-deleted-dialog! handler-id item-id action-props]
                         (if-not (r item-handler/handling-item? db handler-id item-id)
                                 [:item-lister/reload-items! lister-id {:display-progress? true}]
                                 (if-let [next-item-id (r update.subs/get-next-item-id db lister-id item-id)]
                                         (let [next-route (str base-route "/" next-item-id)
                                               on-stalled [:x.router/go-to! next-route]]
                                              [:item-lister/reload-items! lister-id {:on-stalled on-stalled :display-progress? true}])
                                         (let [on-stalled [:x.router/go-to! base-route]]
                                              [:item-lister/reload-items! lister-id {:on-stalled on-stalled :display-progress? true}])))]})))

(r/reg-event-fx :item-handler/delete-item-failed
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id action-props server-response]]
      (let [item-id (r item-handler/get-deleted-item-id db handler-id server-response)]
           {:dispatch-n [[:item-handler/render-delete-item-failed-dialog! handler-id item-id action-props]
                         [:x.ui/end-listening-to-process!]]})))

(r/reg-event-fx :item-handler/render-item-deleted-dialog!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  (fn [_ [_ handler-id item-id action-props]]
      [:x.ui/render-bubble! :item-handler/item-deleted-dialog
                            {:content [update.views/item-deleted-dialog handler-id item-id action-props]}]))

(r/reg-event-fx :item-handler/render-delete-item-failed-dialog!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  (fn [_ [_ handler-id item-id action-props]]
      [:x.ui/render-bubble! :item-handler/delete-item-failed-dialog
                            {:content [update.views/delete-item-failed-dialog handler-id item-id action-props]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/delete-item-undid
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; {:base-route (string)
  ;  :lister-id (keyword)}
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id {:keys [base-route lister-id]} server-response]]
      (let [item-id    (r item-handler/get-recovered-item-id db handler-id server-response)
            item-route (str base-route "/" item-id)]
           {:dispatch-n [[:x.router/go-to! item-route]
                         [:item-lister/reload-items! lister-id]]})))

(r/reg-event-fx :item-handler/undo-delete-item-failed
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  ; @param (map) server-response
  (fn [_ [_ handler-id item-id action-props _]]
      ; XXX#0409 (source-code/cljs/engines/item_handler/update/subs.cljs)
      ; XXX#0409
      ; The [:item-handler/undo-delete-item-failed ...] event has to get the item ID
      ; as a parameter, because the server response could be nil or deficient!
      ; This case is common in failured recovery events, therefore every failured
      ; recovery event has to get the item ID as a parameter.
      {:dispatch-n [[:item-handler/render-undo-delete-item-failed-dialog! handler-id item-id action-props]
                    [:x.ui/end-listening-to-process!]]}))

(r/reg-event-fx :item-handler/render-undo-delete-item-failed-dialog!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  (fn [_ [_ handler-id item-id action-props]]
      [:x.ui/render-bubble! :item-handler/undo-delete-item-failed-dialog
                            {:content [update.views/undo-delete-item-failed-dialog handler-id item-id action-props]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/item-duplicated
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; {:lister-id (keyword)}
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id {:keys [lister-id] :as action-props} server-response]]
      (let [copy-id (r item-handler/get-copy-item-id db handler-id server-response)]
           {:dispatch-n [[:item-handler/render-item-duplicated-dialog! handler-id copy-id action-props]
                         [:item-lister/reload-items! lister-id {:display-progress? true}]]})))

(r/reg-event-fx :item-handler/duplicate-item-failed
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; @param (map) server-response
  (fn [_ [_ handler-id action-props _]]
      ; There is no copy-id if the duplication is failed!
      [:item-handler/render-duplicate-item-failed-dialog! handler-id nil action-props]))

(r/reg-event-fx :item-handler/render-item-duplicated-dialog!
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  ; @param (map) action-props
  (fn [_ [_ handler-id copy-id action-props]]
      [:x.ui/render-bubble! :item-handler/item-duplicated-dialog
                            {:content [update.views/item-duplicated-dialog handler-id copy-id action-props]}]))

(r/reg-event-fx :item-handler/render-duplicate-item-failed-dialog!
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  ; @param (map) action-props
  (fn [_ [_ handler-id copy-id action-props]]
      [:x.ui/render-bubble! :item-handler/duplicate-item-failed-dialog
                            {:content [update.views/duplicate-item-failed-dialog handler-id copy-id action-props]}]))

(r/reg-event-fx :item-handler/view-duplicated-item!
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  ; @param (map) action-props
  ; {:base-route (string)}
  (fn [_ [_ handler-id copy-id {:keys [base-route]}]]
      (let [copy-route (str base-route "/" copy-id)]
           {:dispatch-n [[:x.ui/remove-bubble! :item-handler/item-duplicated-dialog]
                         [:x.router/go-to! copy-route]]})))
