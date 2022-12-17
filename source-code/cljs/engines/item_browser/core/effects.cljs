
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.core.effects
    (:require [engines.item-browser.core.events :as core.events]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/filter-items!
  ; @param (keyword) browser-id
  ; @param (map) filter-pattern
  ;
  ; @usage
  ; [:item-browser/filter-items! :my-browser {...}]
  (fn [{:keys [db]} [_ browser-id filter-pattern]]
      {:db (r core.events/filter-items! db browser-id filter-pattern)
       :fx [:infinite-loader/reload-loader! browser-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) search-props
  ; {:search-keys (keywords in vector)}
  ; @param (string) search-term
  (fn [_ [_ browser-id search-props search-term]]
      [:item-lister/search-items! browser-id search-props search-term]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/order-items!
  ; @param (keyword) browser-id
  ; @param (keyword or namespaced keyword) order-by
  ;
  ; @usage
  ; [:item-browser/order-items! :my-browser :name]
  ;
  ; @usage
  ; [:item-browser/order-items! :my-browser :name/descending]
  (fn [{:keys [db]} [_ browser-id order-by]]
      [:item-lister/order-items! browser-id order-by]))

(r/reg-event-fx :item-browser/swap-items!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; [:item-browser/swap-items! :my-browser]
  (fn [{:keys [db]} [_ browser-id]]
      [:item-lister/swap-items! browser-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/choose-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) order-by-props
  ; {:order-by-options (namespaced keywords in vector)}
  (fn [_ [_ browser-id order-by-props]]
      [:item-lister/choose-order-by! browser-id order-by-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      {:db         (r core.events/load-browser! db browser-id)
       :dispatch-n [[:item-browser/request-items! browser-id]
                    [:item-browser/request-item!  browser-id]]}))

(r/reg-event-fx :item-browser/reload-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      {:db         (r core.events/reload-browser! db browser-id)
       :dispatch-n [[:item-browser/request-items! browser-id]
                    [:item-browser/request-item!  browser-id]]}))
