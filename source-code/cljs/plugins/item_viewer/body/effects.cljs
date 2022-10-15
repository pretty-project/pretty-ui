
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.effects
    (:require [plugins.item-viewer.body.events :as body.events]
              [reagent.api                     :as reagent]
              [re-frame.api                    :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ viewer-id body-props]]
      {:db       (r body.events/body-did-mount db viewer-id body-props)
       :dispatch [:item-viewer/load-viewer! viewer-id]}))

(r/reg-event-fx :item-viewer/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      {:db (r body.events/body-will-unmount db viewer-id)}))

(r/reg-event-fx :item-viewer/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (?) %
  (fn [{:keys [db]} [_ viewer-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db (r body.events/body-did-update db viewer-id body-props)})))
