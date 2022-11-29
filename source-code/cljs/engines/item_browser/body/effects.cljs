
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.body.effects
    (:require [engines.item-browser.body.events :as body.events]
              [plugins.reagent.api              :as reagent]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ browser-id body-props]]
      {:db       (r body.events/body-did-mount db browser-id body-props)
       :dispatch [:item-browser/load-browser! browser-id]}))

(r/reg-event-fx :item-browser/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      {:db (r body.events/body-will-unmount db browser-id)}))

(r/reg-event-fx :item-browser/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (?) %
  (fn [{:keys [db]} [_ browser-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db       (r body.events/body-did-update db browser-id body-props)
            :dispatch [:infinite-loader/reload-loader! browser-id]})))
