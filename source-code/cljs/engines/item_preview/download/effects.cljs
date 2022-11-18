
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.download.effects
    (:require [engines.item-preview.body.subs           :as body.subs]
              [engines.item-preview.core.subs           :as core.subs]
              [engines.item-preview.download.events     :as download.events]
              [engines.item-preview.download.queries    :as download.queries]
              [engines.item-preview.download.subs       :as download.subs]
              [engines.item-preview.download.validators :as download.validators]
              [re-frame.api                             :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-preview/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  (fn [{:keys [db]} [_ preview-id]]
      (if (r download.subs/request-item? db preview-id)
          (let [query        (r download.queries/get-request-item-query          db preview-id)
                validator-f #(r download.validators/request-item-response-valid? db preview-id %)]
               {:db       (r download.events/request-item! db preview-id)
                :dispatch [:pathom/send-query! (r core.subs/get-request-id db preview-id)
                                               ; XXX#4057 (source-code/cljs/engines/item_handler/download/effects.cljs)
                                               {:display-progress? false
                                                :on-stalled [:item-preview/receive-item!     preview-id]
                                                :on-failure [:item-preview/set-engine-error! preview-id :failed-to-request-item]
                                                :query query :validator-f validator-f}]}))))

(r/reg-event-fx :item-preview/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ preview-id server-response]]
      ; Ha az [:item-preview/receive-item! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db preview-id)
          {:db (r download.events/receive-item! db preview-id server-response)})))
