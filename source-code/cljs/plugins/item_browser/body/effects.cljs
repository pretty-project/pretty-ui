

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.effects
    (:require [plugins.item-browser.body.events :as body.events]
              [reagent.api                      :as reagent]
              [x.app-core.api                   :as a :refer [r]]))



;; -- Body lifecycles effects -------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ browser-id body-props]]
      {:db       (r body.events/body-did-mount db browser-id body-props)
       :dispatch [:item-browser/load-browser! browser-id]}))

(a/reg-event-fx
  :item-browser/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      {:db (r body.events/body-will-unmount db browser-id)}))

(a/reg-event-fx
  :item-browser/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (?) %
  (fn [{:keys [db]} [_ browser-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db       (r body.events/body-did-update db browser-id body-props)
            :dispatch [:tools/reload-infinite-loader! browser-id]})))
