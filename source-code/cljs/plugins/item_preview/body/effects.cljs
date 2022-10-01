
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.body.effects
    (:require [plugins.item-preview.body.events :as body.events]
              [reagent.api                      :as reagent]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-preview/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ preview-id body-props]]
      {:db       (r body.events/body-did-mount db preview-id body-props)
       :dispatch [:item-preview/load-preview! preview-id]}))

(r/reg-event-fx
  :item-preview/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  (fn [{:keys [db]} [_ preview-id]]
      {:db (r body.events/body-will-unmount db preview-id)}))

(r/reg-event-fx
  :item-preview/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (?) %
  (fn [{:keys [db]} [_ preview-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db       (r body.events/body-did-update db preview-id body-props)
            :dispatch [:item-preview/reload-preview! preview-id]})))
