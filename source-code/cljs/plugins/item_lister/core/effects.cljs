
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events   :as core.events]
              [plugins.item-lister.items.events  :as items.events]
              [plugins.item-lister.items.subs    :as items.subs]
              [plugins.item-lister.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      (let [on-route    (r transfer.subs/get-transfer-item db lister-id :on-route)
            route-title (r transfer.subs/get-transfer-item db lister-id :route-title)]
           {;:db (r core.events/handle-route! db lister-id)
            :dispatch-n [on-route (if route-title [:ui/set-title! route-title])]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/use-filter!
  ; @param (keyword) lister-id
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-lister/use-filter! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id filter-pattern]]
      {:db (r core.events/use-filter! db lister-id filter-pattern)
       :dispatch [:tools/reload-infinite-loader! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db (r core.events/reset-downloads! db lister-id)
       :dispatch [:tools/reload-infinite-loader! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db (r core.events/reset-downloads! db lister-id)
       :dispatch [:tools/reload-infinite-loader! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/item-clicked
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) options
  ;  {:on-click (metamorphic-event)}
  ;
  ; @usage
  ;  [:item-lister/item-clicked :my-lister 0 {...}]
  (fn [{:keys [db]} [_ lister-id item-dex {:keys [on-click]}]]
      ; A) ...
      ;
      ; B) ...
      (if (r items.subs/toggle-item-selection? db lister-id item-dex)
          ; A)
          {:db (r items.events/toggle-item-selection! db lister-id item-dex)}
          ; B)
          {:dispatch on-click})))
