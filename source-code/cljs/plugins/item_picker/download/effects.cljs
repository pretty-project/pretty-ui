
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.download.effects
    (:require [plugins.item-picker.body.subs           :as body.subs]
              [plugins.item-picker.core.subs           :as core.subs]
              [plugins.item-picker.download.events     :as download.events]
              [plugins.item-picker.download.queries    :as download.queries]
              [plugins.item-picker.download.validators :as download.validators]
              [re-frame.api                            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-picker/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  (fn [{:keys [db]} [_ picker-id]]
      (let [query        (r download.queries/get-request-item-query          db picker-id)
            validator-f #(r download.validators/request-item-response-valid? db picker-id %)]
           {:db       (r download.events/request-item! db picker-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db picker-id)
                                           {:on-success [:item-picker/receive-item!   picker-id]
                                            :on-failure [:item-picker/set-error-mode! picker-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx
  :item-picker/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ picker-id server-response]]
      ; Ha az [:item-picker/receive-item! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db picker-id)
          {:db       (r download.events/receive-item! db picker-id server-response)
           :dispatch [:item-picker/item-received picker-id]})))

(r/reg-event-fx
  :item-picker/item-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  (fn [{:keys [db]} [_ picker-id]]))
