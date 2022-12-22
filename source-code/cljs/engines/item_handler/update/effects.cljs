
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.effects
    (:require [engines.item-handler.backup.events     :as backup.events]
              [engines.item-handler.body.subs         :as body.subs]
              [engines.item-handler.core.subs         :as core.subs]
              [engines.item-handler.update.queries    :as update.queries]
              [engines.item-handler.update.validators :as update.validators]
              [re-frame.api                           :as r :refer [r]]))



;; -- Save item effects -------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/save-item!
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; {:display-progress? (boolean)(opt)
  ;   Default: false
  ;  :on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)
  ;  :on-stalled (metamorphic-event)(opt)
  ;  :progress-behaviour (keyword)(opt)
  ;   :keep-faked, :normal
  ;   Default: :normal
  ;   W/ {:display-progress? true}}
  ;  :progress-max (percent)(opt)
  ;   Default: 100
  ;   W/ {:display-progress? true}}
  ;
  ; @usage
  ; [:item-handler/save-item! :my-handler]
  (fn [{:keys [db]} [_ handler-id action-props]]
      (let [query        (r update.queries/get-save-item-query          db handler-id)
            validator-f #(r update.validators/save-item-response-valid? db handler-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                (assoc action-props :query query :validator-f)])))



;; -- Delete item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/delete-item!
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; {:display-progress? (boolean)(opt)
  ;   Default: false
  ;  :on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)
  ;  :on-stalled (metamorphic-event)(opt)
  ;  :progress-behaviour (keyword)(opt)
  ;   :keep-faked, :normal
  ;   Default: :normal
  ;   W/ {:display-progress? true}}
  ;  :progress-max (percent)(opt)
  ;   Default: 100
  ;   W/ {:display-progress? true}}
  ;
  ; @usage
  ; [:item-handler/delete-item! :my-handler]
  (fn [{:keys [db]} [_ handler-id action-props]]
      (let [query        (r update.queries/get-delete-item-query          db handler-id)
            validator-f #(r update.validators/delete-item-response-valid? db handler-id %)]
           {:db       (r backup.events/backup-current-item! db handler-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                           (assoc action-props :query query :validator-f)]})))



;; -- Undo delete item effects ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/undo-delete-item!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) action-props
  ; {:display-progress? (boolean)(opt)
  ;   Default: false
  ;  :on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)
  ;  :on-stalled (metamorphic-event)(opt)
  ;  :progress-behaviour (keyword)(opt)
  ;   :keep-faked, :normal
  ;   Default: :normal
  ;   W/ {:display-progress? true}}
  ;  :progress-max (percent)(opt)
  ;   Default: 100
  ;   W/ {:display-progress? true}}
  ;
  ; @usage
  ; [:item-handler/undo-delete-item! :my-handler "my-item"]
  (fn [{:keys [db]} [_ handler-id item-id action-props]]
      ; Az [:item-handler/undo-delete-item! ...] esemény ...
      ; ...
      (let [query        (r update.queries/get-undo-delete-item-query          db handler-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db handler-id %)]
           {:dispatch-n [[:x.ui/remove-bubble! ::item-deleted-dialog]
                         [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                              (assoc action-props :query query :validator-f)]]})))



;; -- Duplicate item effects --------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/duplicate-item!
  ; @param (keyword) handler-id
  ; @param (map) action-props
  ; {:display-progress? (boolean)(opt)
  ;   Default: false
  ;  :on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)
  ;  :on-stalled (metamorphic-event)(opt)
  ;  :progress-behaviour (keyword)(opt)
  ;   :keep-faked, :normal
  ;   Default: :normal
  ;   W/ {:display-progress? true}}
  ;  :progress-max (percent)(opt)
  ;   Default: 100
  ;   W/ {:display-progress? true}}
  ;
  ; @usage
  ; [:item-handler/duplicate-item! :my-handler]
  (fn [{:keys [db]} [_ handler-id action-props]]
      (let [display-progress? (r body.subs/get-body-prop                          db handler-id :display-progress?)
            query             (r update.queries/get-duplicate-item-query          db handler-id)
            validator-f      #(r update.validators/duplicate-item-response-valid? db handler-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                (assoc action-props :query query :validator-f)])))
