
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.body.effects
    (:require [engines.item-viewer.body.events :as body.events]
              [re-frame.api                    :as r :refer [r]]
              [reagent.api                     :as reagent]))



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
