
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.update.effects
    (:require [engines.item-viewer.body.subs         :as body.subs]
              [engines.item-viewer.core.subs         :as core.subs]
              [engines.item-viewer.update.events     :as update.events]
              [engines.item-viewer.update.queries    :as update.queries]
              [engines.item-viewer.update.validators :as update.validators]
              [re-frame.api                          :as r :refer [r]]
              [x.ui.api                              :as x.ui]))



;; -- Delete item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/delete-item!
  ; @param (keyword) viewer-id
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
  ; [:item-viewer/delete-item! :my-viewer]
  ;
  ; @usage
  ; [:item-viewer/delete-item! :my-viewer {...}]
  (fn [{:keys [db]} [_ viewer-id action-props]]
      (let [query        (r update.queries/get-delete-item-query          db viewer-id)
            validator-f #(r update.validators/delete-item-response-valid? db viewer-id %)]
           {:db       (r update.events/delete-item! db viewer-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                           (assoc action-props :query query :validator-f validator-f)]})))



;; -- Undo delete item effects ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/undo-delete-item!
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ; @param (map)(opt) action-props
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
  ; [:item-viewer/undo-delete-item! :my-viewer]
  ;
  ; @usage
  ; [:item-viewer/undo-delete-item! :my-viewer {...}]
  (fn [{:keys [db]} [_ viewer-id item-id action-props]]
      (let [query        (r update.queries/get-undo-delete-item-query          db viewer-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db viewer-id %)]
           {:db         (r x.ui/fake-progress! db 15)
            :dispatch-n [[:x.ui/remove-bubble! ::item-deleted-dialog]
                         [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                              (assoc action-props :query query :validator-f)]]})))



;; -- Duplicate item effects --------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/duplicate-item!
  ; @param (keyword) viewer-id
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
  ; [:item-viewer/duplicate-item! :my-viewer]
  ;
  ; @usage
  ; [:item-viewer/duplicate-item! :my-viewer {...}]
  (fn [{:keys [db]} [_ viewer-id action-props]]
      (let [display-progress? (r body.subs/get-body-prop                          db viewer-id :display-progress?)
            query             (r update.queries/get-duplicate-item-query          db viewer-id)
            validator-f      #(r update.validators/duplicate-item-response-valid? db viewer-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                (assoc action-props :query query :validator-f)])))
