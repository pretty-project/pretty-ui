
(ns templates.item-browser.update.effects
    (:require [engines.item-browser.api            :as item-browser]
              [templates.item-browser.update.views :as update.views]
              [re-frame.api                        :as r :refer [r]]))

;; -- Update item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/item-updated
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [_ [_ _ _]]))

(r/reg-event-fx :item-browser/update-item-failed
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [_ [_ _ _]]
      [:x.ui/render-bubble! {:body :failed-to-update}]))

;; -- Delete item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/item-deleted
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      (let [item-id (r item-browser/get-deleted-item-id db browser-id server-response)]
           (if (r item-browser/item-listed? db browser-id item-id)
               (let [on-reload [:item-browser/render-item-deleted-dialog! browser-id item-id]]
                    [:item-browser/reload-items! browser-id {:on-reload on-reload}])
               [:item-browser/render-item-deleted-dialog! browser-id item-id]))))

(r/reg-event-fx :item-browser/delete-item-failed
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [_ [_ browser-id item-id _]]
      ; XXX#0409 (source-code/cljs/templates/item_viewer/update/effects.cljs)
      {:dispatch-n [[:item-browser/render-delete-item-failed-dialog! browser-id item-id]
                    [:x.ui/end-listening-to-process!]]}))

(r/reg-event-fx :item-browser/render-delete-item-failed-dialog!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [_ [_ _ _]]
      [:x.ui/render-bubble! :item-browser/delete-item-failed-dialog {:body :failed-to-delete}]))

(r/reg-event-fx :item-browser/render-item-deleted-dialog!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [_ [_ browser-id item-id]]
      [:x.ui/render-bubble! :item-browser/item-deleted-dialog
                            {:body      [update.views/item-deleted-dialog-body browser-id item-id]}]))
                            ;:on-umount [:item-browser/clean-backup-items! browser-id item-id]

;; -- Undo delete item effects ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/delete-item-undid
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      (let [item-id (r item-browser/get-recovered-item-id db browser-id server-response)]
           (if (r item-browser/parent-item-browsed? db browser-id :undo-delete-item! server-response)
               [:item-browser/reload-items! browser-id]))))

(r/reg-event-fx :item-browser/undo-delete-item-failed
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      (let [item-id (r item-browser/get-recovered-item-id db browser-id server-response)]
           {:dispatch-n [[:item-browser/render-undo-delete-item-failed-dialog! browser-id item-id]
                         [:x.ui/end-listening-to-process!]]})))

(r/reg-event-fx :item-browser/render-undo-delete-item-failed-dialog!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [_ [_ browser-id item-id]]
      [:x.ui/render-bubble! :item-browser/undo-delete-item-failed-dialog
                            {:body [update.views/undo-delete-item-failed-dialog-body browser-id item-id]}]))

;; -- Duplicate item effects --------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/item-duplicated
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      (let [copy-id (r item-browser/get-copy-item-id db browser-id server-response)]
           (if (r item-browser/parent-item-browsed? db browser-id :duplicate-item! server-response)
               (let [on-reload [:item-browser/render-item-duplicated-dialog! browser-id copy-id]]
                    [:item-browser/reload-items! browser-id {:on-reload on-reload}])
               [:item-browser/render-item-duplicated-dialog! browser-id copy-id]))))

(r/reg-event-fx :item-browser/duplicate-item-failed
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [_ [_ browser-id _]]
      ; There is no copy-id if the duplication failed!
      {:dispatch [[:item-browser/render-duplicate-item-failed-dialog! browser-id nil]
                  [:x.ui/end-listening-to-process!]]}))

(r/reg-event-fx :item-browser/render-item-duplicated-dialog!
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  (fn [_ [_ browser-id copy-id]]
      [:x.ui/render-bubble! :item-browser/item-duplicated-dialog
                            {:body [update.views/item-duplicated-dialog-body browser-id copy-id]}]))

(r/reg-event-fx :item-browser/render-duplicate-item-failed-dialog!
  ; @param (keyword) browser-id
  ; @param (nil) copy-id
  (fn [_ [_ _ _]]
      ; There is no copy-id if the duplication failed!
      [:x.ui/render-bubble! :item-browser/duplicate-item-failed-dialog
                            {:body :failed-to-duplicate}]))
