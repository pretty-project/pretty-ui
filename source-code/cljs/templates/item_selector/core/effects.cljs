
(ns templates.item-selector.core.effects
    (:require [engines.item-browser.api :as item-browser]
              [re-frame.api             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-selector/browse-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ browser-id item-id]]
      {:db       (r item-browser/set-item-id! db browser-id item-id)
       :dispatch [:item-browser/load-browser! browser-id]}))

(r/reg-event-fx :item-selector/go-home!
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      (let [default-item-id (r item-browser/get-default-item-id db browser-id)]
           [:item-selector/browse-item! browser-id default-item-id])))

(r/reg-event-fx :item-selector/go-up!
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      (let [parent-item-id (r item-browser/get-parent-item-id db browser-id)]
           [:item-selector/browse-item! browser-id parent-item-id])))
