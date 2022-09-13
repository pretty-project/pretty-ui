
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.body.effects
    (:require [plugins.item-lister.body.events :as body.events]
              [reagent.api                     :as reagent]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ lister-id body-props]]
      {:db       (r body.events/body-did-mount db lister-id body-props)
       :dispatch [:item-lister/request-items! lister-id]}))

(a/reg-event-fx
  :item-lister/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db (r body.events/body-will-unmount db lister-id)}))

(a/reg-event-fx
  :item-lister/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (?) %
  (fn [{:keys [db]} [_ lister-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db       (r body.events/body-did-update db lister-id body-props)
            :dispatch [:item-lister/request-items! lister-id]})))
