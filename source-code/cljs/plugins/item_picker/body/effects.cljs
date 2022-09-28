
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.body.effects
    (:require [plugins.item-picker.body.events :as body.events]
              [reagent.api                     :as reagent]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-picker/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ picker-id body-props]]
      {:db       (r body.events/body-did-mount db picker-id body-props)
       :dispatch [:item-picker/load-editor! picker-id]}))

(a/reg-event-fx
  :item-picker/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  (fn [{:keys [db]} [_ picker-id]]
      {:db (r body.events/body-will-unmount db editor-id)}))

(a/reg-event-fx
  :item-editor/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (?) %
  (fn [{:keys [db]} [_ picker-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db (r body.events/body-did-update db picker-id body-props)})))
