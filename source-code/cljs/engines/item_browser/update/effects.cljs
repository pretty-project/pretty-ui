
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.update.effects
    (:require [engines.item-browser.body.subs         :as body.subs]
              [engines.item-browser.core.subs         :as core.subs]
              [engines.item-browser.update.events     :as update.events]
              [engines.item-browser.update.queries    :as update.queries]
              [engines.item-browser.update.validators :as update.validators]
              [re-frame.api                           :as r :refer [r]]
              [x.ui.api                               :as x.ui]))



;; -- Update item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/update-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) item-changes
  ; @param (map) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-browser/update-item! :my-browser "my-item" {...}]
  (fn [{:keys [db]} [_ browser-id item-id item-changes {:keys [on-failure on-success]}]]
      ; - Az [:item-browser/update-item! ...] esemény az item-changes paraméterként átadott változásokat
      ;   azonnal végrahajta az elemen.
      ;
      ; - Ha az elem szerver-oldali változatának felülírása sikertelen volt, akkor a kliens-oldali
      ;   változat a tárolt biztonsági mentésből helyreállítódik.
      ;
      ; - Egy időben egy változtatást lehetséges az elemen végrehajtani, mert egy darab biztonsági
      ;   mentéssel nem lehetséges az időben átfedésbe kerülő változtatásokat kezelni, ezért a szerver
      ;   válaszának megérkezéséig az elem {:disabled? true} állapotban van.
      (let [db                (r update.events/update-item!                    db browser-id item-id item-changes)
            display-progress? (r body.subs/get-body-prop                       db browser-id :display-progress?)
            query             (r update.queries/get-update-item-query          db browser-id item-id)
            validator-f      #(r update.validators/update-item-response-valid? db browser-id %)]
           {:db db :dispatch [:pathom/send-query! :storage.media-browser/update-item!
                                                  {:display-progress? display-progress?
                                                   :on-success        {:dispatch-n [on-success [:item-browser/enable-item!    browser-id item-id]]}
                                                   :on-failure        {:dispatch-n [on-failure [:item-browser/revert-changes! browser-id item-id]
                                                                                               [:item-browser/enable-item!    browser-id item-id]]}
                                                   :query             query
                                                   :validator-f       validator-f}]})))



;; -- Delete item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/delete-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-browser/delete-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id {:keys [on-failure on-success]}]]
      (let [query        (r update.queries/get-delete-item-query          db browser-id item-id)
            validator-f #(r update.validators/delete-item-response-valid? db browser-id %)]
           {:db       (r update.events/delete-item! db browser-id item-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db browser-id)
                                           {:display-progress? false
                                            :on-success        on-success
                                            :on-failure        {:dispatch-n [on-failure [:item-browser/enable-item! browser-id item-id]]}
                                            :query             query
                                            :validator-f       validator-f}]})))



;; -- Undo delete item effects ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/undo-delete-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-browser/undo-delete-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id {:keys [on-failure on-success]}]]
      (let [query        (r update.queries/get-undo-delete-item-query          db browser-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db browser-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch-n [[:x.ui/remove-bubble! ::item-deleted-dialog]
                         [:pathom/send-query! (r core.subs/get-request-id db browser-id)
                                              {:display-progress? false
                                               :on-success        on-success
                                               :on-failure        on-failure
                                               :query             query
                                               :validator-f       validator-f}]]})))



;; -- Duplicate item effects --------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/duplicate-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-browser/duplicate-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id {:keys [on-failure on-success]}]]
      (let [query        (r update.queries/get-duplicate-item-query          db browser-id item-id)
            validator-f #(r update.validators/duplicate-item-response-valid? db browser-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db browser-id)
                                           {:display-progress? false
                                            :on-success        on-success
                                            :on-failure        on-failure
                                            :query             query
                                            :validator-f       validator-f}]})))



;; -- Move item effects -------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/move-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (string) destination-id
  ; @param (map) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-browser/move-item! :my-browser "my-item" "your-item"]
  (fn [{:keys [db]} [_ browser-id item-id destination-id {:keys [on-failure on-success]}]]))
