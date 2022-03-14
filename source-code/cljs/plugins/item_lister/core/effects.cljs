
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events     :as core.events]
              [plugins.item-lister.core.subs       :as core.subs]
              [plugins.item-lister.download.events :as download.events]
              [x.app-core.api                      :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [on-load (r core.subs/get-meta-item db extension-id item-namespace :on-load)]
           {:db (r core.events/load-lister! db extension-id item-namespace)
            :dispatch on-load})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  (fn [{:keys [db]} [_ extension-id item-namespace body-props]]
      {:db (r core.events/body-did-mount db extension-id item-namespace body-props)
       ; XXX#5660
       ; Az :item-lister/keypress-listener figyelő biztosítja, hogy a keypress-handler aktív legyen.
       :dispatch [:environment/reg-keypress-listener! :item-lister/keypress-listener]}))

(a/reg-event-fx
  :item-lister/header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  (fn [{:keys [db]} [_ extension-id item-namespace header-props]]
      {:db (r core.events/header-did-mount db extension-id item-namespace header-props)}))

(a/reg-event-fx
  :item-lister/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r core.events/body-will-unmount db extension-id item-namespace)
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
