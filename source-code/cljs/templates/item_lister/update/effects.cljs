
(ns templates.item-lister.update.effects
    (:require [engines.item-lister.api            :as item-lister]
              [re-frame.api                       :as r :refer [r]]
              [templates.item-lister.update.views :as update.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/items-reordered
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [_ [_ lister-id _]]
      [:item-lister/render-items-reordered-dialog! lister-id]))

(r/reg-event-fx :item-lister/reorder-items-failed
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [_ [_ lister-id _]]
      [:item-lister/render-reorder-items-failed-dialog! lister-id]))

(r/reg-event-fx :item-lister/render-items-reordered-dialog!
  ; @param (keyword) lister-id
  (fn [_ [_ lister-id]]
      [:x.ui/render-bubble! :item-lister/items-reordered-dialog
                            {:content [update.views/items-reordered-dialog lister-id]}]))

(r/reg-event-fx :item-lister/render-reorder-items-failed-dialog!
  ; @param (keyword) lister-id
  (fn [_ [_ lister-id]]
      [:x.ui/render-bubble! :item-lister/reorder-items-failed-dialog
                            {:content [update.views/reorder-items-failed-dialog lister-id]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/items-deleted
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      (let [item-ids (r item-lister/get-deleted-item-ids db lister-id server-response)]
           (if (r item-lister/body-did-mount? db lister-id)
               (let [on-reload [:item-lister/render-items-deleted-dialog! lister-id item-ids]]
                    [:item-lister/reload-items! lister-id {:on-reload on-reload}])
               [:item-lister/render-items-deleted-dialog! lister-id item-ids]))))

(r/reg-event-fx :item-lister/delete-items-failed
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [_ [_ lister-id _]]
      [:item-lister/render-delete-items-failed-dialog! lister-id nil]))

(r/reg-event-fx :item-lister/render-items-deleted-dialog!
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  (fn [_ [_ lister-id item-ids]]
      [:x.ui/render-bubble! :item-lister/items-deleted-dialog
                            {:content [update.views/items-deleted-dialog lister-id item-ids]}]))

(r/reg-event-fx :item-lister/render-delete-items-failed-dialog!
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  (fn [_ [_ lister-id item-ids]]
      [:x.ui/render-bubble! :item-lister/delete-items-failed-dialog
                            {:content [update.views/delete-items-failed-dialog lister-id item-ids]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/delete-items-undid
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      (if (r item-lister/body-did-mount? db lister-id)
          [:item-lister/reload-items! lister-id])))

(r/reg-event-fx :item-lister/undo-delete-items-failed
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ; @param (map) server-response
  (fn [_ [_ lister-id item-ids _]]
      ; XXX#0439 (source-code/cljs/templates/item_lister/update/README.md)
      [:item-lister/render-undo-delete-items-failed-dialog! lister-id item-ids]))

(r/reg-event-fx :item-lister/render-undo-delete-items-failed-dialog!
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  (fn [_ [_ lister-id item-ids]]
      [:x.ui/render-bubble! :item-lister/undo-delete-items-failed-dialog
                            {:content [update.views/undo-delete-items-failed-dialog lister-id item-ids]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/items-duplicated
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      (let [copy-ids (r item-lister/get-duplicated-item-ids db lister-id server-response)]
           (if (r item-lister/body-did-mount? db lister-id)
               (let [on-reload [:item-lister/render-items-duplicated-dialog! lister-id copy-ids]]
                    [:item-lister/reload-items! lister-id {:on-reload on-reload}])
               [:item-lister/render-items-duplicated-dialog! lister-id copy-ids]))))

(r/reg-event-fx :item-lister/duplicate-items-failed
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      (if (r item-lister/body-did-mount? db lister-id)
          [:item-lister/render-duplicate-items-failed-dialog! lister-id nil])))

(r/reg-event-fx :item-lister/render-items-duplicated-dialog!
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  (fn [_ [_ lister-id copy-ids]]
      [:x.ui/render-bubble! :item-lister/items-duplicated-dialog
                            {:body [update.views/items-duplicated-dialog lister-id copy-ids]}]))

(r/reg-event-fx :item-lister/render-duplicate-items-failed-dialog!
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  (fn [_ [_ lister-id copy-ids]]
      [:x.ui/render-bubble! :item-lister/duplicate-items-failed-dialog
                            {:content [update.views/duplicate-items-failed-dialog lister-id copy-ids]}]))
