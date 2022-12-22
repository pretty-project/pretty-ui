
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.body.effects
    (:require [engines.item-handler.body.events :as body.events]
              [engines.item-handler.core.subs   :as core.subs]
              [re-frame.api                     :as r :refer [r]]
              [reagent.api                      :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ handler-id body-props]]
      {:db       (r body.events/body-did-mount db handler-id body-props)
       :dispatch [:item-handler/load-handler! handler-id]}))

(r/reg-event-fx :item-handler/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      {:db (r body.events/body-will-unmount db handler-id)}))

(r/reg-event-fx :item-handler/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (?) %
  (fn [{:keys [db]} [_ handler-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           (if (r core.subs/reload-item? db handler-id body-props)
               {:dispatch [:item-handler/reload-handler! handler-id]
                :db       (r body.events/body-did-update db handler-id body-props)}
               {:db       (r body.events/body-did-update db handler-id body-props)}))))
