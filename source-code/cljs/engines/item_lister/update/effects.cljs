
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.update.effects
    (:require [engines.item-lister.body.subs         :as body.subs]
              [engines.item-lister.core.subs         :as core.subs]
              [engines.item-lister.selection.subs    :as selection.subs]
              [engines.item-lister.update.events     :as update.events]
              [engines.item-lister.update.queries    :as update.queries]
              [engines.item-lister.update.validators :as update.validators]
              [re-frame.api                          :as r :refer [r]]
              [x.ui.api                              :as x.ui]))



;; -- Reorder items effects ---------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/reorder-items!
  ; @param (keyword) lister-id
  ; @param (maps in vector) reordered-items
  ; @param (map)(opt) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-lister/reorder-items! :my-lister [{...} {...}]]
  (fn [{:keys [db]} [_ lister-id reordered-items {:keys [on-failure on-success]}]]
      (let [db                (r update.events/reorder-items!                    db lister-id reordered-items)
            display-progress? (r body.subs/get-body-prop                         db lister-id :display-progress?)
            query             (r update.queries/get-reorder-items-query          db lister-id)
            validator-f      #(r update.validators/reorder-items-response-valid? db lister-id %)]
           {:db db :dispatch [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                                  {:display-progress? display-progress?
                                                   :on-success        on-success
                                                   :on-failure        on-failure
                                                   :query             query
                                                   :validator-f       validator-f}]})))



;; -- Delete items effects ----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/delete-selected-items!
  ; @param (keyword) lister-id
  ; @param (map)(opt) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-lister/delete-selected-items! :my-lister]
  (fn [{:keys [db]} [_ lister-id {:keys [on-failure on-success]}]]
      (let [item-ids     (r selection.subs/export-selection                db lister-id)
            query        (r update.queries/get-delete-items-query          db lister-id item-ids)
            validator-f #(r update.validators/delete-items-response-valid? db lister-id %)]
           {:db       (r update.events/delete-selected-items! db lister-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                           {:display-progress? false
                                            :on-success        on-success
                                            :on-failure        {:dispatch-n [on-failure [:item-lister/enable-items! lister-id item-ids]]}
                                            :query             query
                                            :validator-f       validator-f}]})))



;; -- Undo delete items effects -----------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/undo-delete-items!
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ; @param (map)(opt) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-lister/undo-delete-items! :my-lister ["my-item" "your-item"]]
  (fn [{:keys [db]} [_ lister-id item-ids {:keys [on-failure on-success]}]]
      (let [query        (r update.queries/get-undo-delete-items-query          db lister-id item-ids)
            validator-f #(r update.validators/undo-delete-items-response-valid? db lister-id %)]
           {:db         (r x.ui/fake-process! db 15)
            :dispatch-n [[:x.ui/remove-bubble! ::items-deleted-dialog]
                         [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                              {:display-progress? false
                                               :on-success        on-success
                                               :on-failure        on-failure
                                               :query             query
                                               :validator-f       validator-f}]]})))



;; -- Duplicate items effects -------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/duplicate-selected-items!
  ; @param (keyword) lister-id
  ; @param (map)(opt) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-lister/duplicate-selected-items! :my-lister]
  (fn [{:keys [db]} [_ lister-id {:keys [on-failure on-success]}]]
      (let [item-ids     (r selection.subs/export-selection                   db lister-id)
            query        (r update.queries/get-duplicate-items-query          db lister-id item-ids)
            validator-f #(r update.validators/duplicate-items-response-valid? db lister-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                           {:display-progress? false
                                            :on-success        on-success
                                            :on-failure        on-failure
                                            :query             query
                                            :validator-f       validator-f}]})))
