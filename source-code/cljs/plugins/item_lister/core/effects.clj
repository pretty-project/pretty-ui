
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events :as core.events]
              [plugins.item-lister.core.subs   :as core.subs]
              [x.app-core.api                  :as a :refer [r]]
              [x.app-ui.api                    :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [route-title (r core.subs/get-meta-item db extension-id item-namespace :route-title)
            on-load     (r core.subs/get-meta-item db extension-id item-namespace :on-load)]
           {:db (if-not route-title db (r ui/set-header-title! db route-title))
            :dispatch-n [on-load (if route-title [:ui/set-window-title! route-title])]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/init-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  (fn [{:keys [db]} [_ extension-id item-namespace body-props]]
      {:db (r core.events/init-body! db extension-id item-namespace body-props)
       ; XXX#5660
       ; Az :item-lister/keypress-listener figyelő biztosítja, hogy a keypress-handler aktív legyen.
       :dispatch [:environment/reg-keypress-listener! :item-lister/keypress-listener]}))

(a/reg-event-fx
  :item-lister/init-header!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  (fn [{:keys [db]} [_ extension-id item-namespace header-props]]
      {:db (r core.events/init-header! db extension-id item-namespace header-props)}))

(a/reg-event-fx
  :item-lister/destruct-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r core.events/destruct-body! db extension-id item-namespace)
       :dispatch-n [; XXX#5660
                    [:environment/remove-keypress-listener! :item-lister/keypress-listener]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/use-filter!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-lister/use-filter! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace filter-pattern]]
      {:db (r core.events/use-filter! db extension-id item-namespace filter-pattern)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r download.events/reset-downloads! db extension-id item-namespace)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r download.events/reset-downloads! db extension-id item-namespace)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))
