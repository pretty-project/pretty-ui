
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.body.effects
    (:require [engines.item-preview.body.events :as body.events]
              [engines.item-preview.body.subs   :as body.subs]
              [engines.item-preview.core.subs   :as core.subs]
              [re-frame.api                     :as r :refer [r]]
              [reagent.api                      :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-preview/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ preview-id body-props]]
      {:db       (r body.events/body-did-mount db preview-id body-props)
       :dispatch [:item-preview/load-preview! preview-id]}))

(r/reg-event-fx :item-preview/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  (fn [{:keys [db]} [_ preview-id]]
      {:db (r body.events/body-will-unmount db preview-id)}))

(r/reg-event-fx :item-preview/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (?) %
  (fn [{:keys [db]} [_ preview-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           (if (r core.subs/reload-item? db preview-id body-props)
               {:dispatch [:item-preview/reload-preview! preview-id]
                :db       (r body.events/body-did-update db preview-id body-props)}
               {:db       (r body.events/body-did-update db preview-id body-props)}))))
