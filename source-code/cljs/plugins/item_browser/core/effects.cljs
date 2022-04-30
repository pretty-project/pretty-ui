
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.effects
    (:require [plugins.item-browser.body.subs    :as body.subs]
              [plugins.item-browser.core.events  :as core.events]
              [plugins.item-browser.core.subs    :as core.subs]
              [plugins.item-browser.items.events :as items.events]
              [plugins.item-browser.items.subs   :as items.subs]
              [plugins.item-browser.routes.subs  :as routes.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id]]
      ; XXX#5575
      (if-let [route-handled? (r routes.subs/route-handled? db browser-id)]
              (let [item-route (r routes.subs/get-item-route db browser-id item-id)]
                   {:dispatch [:router/go-to! item-route]})
              (if (r body.subs/body-did-mount? db browser-id)
                  {:db       (r core.events/set-item-id! db browser-id item-id)
                   :dispatch [:item-browser/load-browser! browser-id]}))))

(a/reg-event-fx
  :item-browser/go-home!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  [:item-browser/go-home! :my-browser]
  (fn [{:keys [db]} [_ browser-id]]
      (let [default-item-id (r body.subs/get-body-prop db browser-id :default-item-id)]
           [:item-browser/browse-item! browser-id default-item-id])))

(a/reg-event-fx
  :item-browser/go-up!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  [:item-browser/go-up! :my-browser]
  (fn [{:keys [db]} [_ browser-id]]
      (let [parent-item-id (r core.subs/get-parent-item-id db browser-id)]
           [:item-browser/browse-item! browser-id parent-item-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/filter-items!
  ; @param (keyword) browser-id
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-browser/filter-items! :my-browser {...}]
  (fn [{:keys [db]} [_ browser-id filter-pattern]]
      {:db       (r core.events/filter-items! db browser-id filter-pattern)
       :dispatch [:tools/reload-infinite-loader! browser-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      {:db         (r core.events/load-browser! db browser-id)
       :dispatch-n [[:item-browser/request-items! browser-id]
                    [:item-browser/request-item!  browser-id]]}))
