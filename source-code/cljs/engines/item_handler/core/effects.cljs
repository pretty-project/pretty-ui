
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.core.effects
    (:require [engines.item-handler.core.events :as core.events]
              [engines.item-handler.core.subs   :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/load-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      ; XXX#1309
      ; The current item's ID has to be derived before any other function applied,
      ; otherwise the functions might use the previous item's ID.
      (let [db (r core.events/derive-item-id! db handler-id)]
           (if (or (r core.subs/current-item-downloaded? db handler-id)
                   (r core.subs/no-item-id-passed?       db handler-id))
               {:db       (r core.events/load-handler! db handler-id)}
               {:db       (r core.events/load-handler! db handler-id)
                :dispatch [:item-handler/request-item! handler-id]}))))

(r/reg-event-fx :item-handler/reload-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      ; XXX#1309
      (let [db (r core.events/derive-item-id! db handler-id)]
           (if (or (r core.subs/current-item-downloaded? db handler-id)
                   (r core.subs/no-item-id-passed?       db handler-id))
               {:db       (r core.events/reload-handler! db handler-id)}
               {:db       (r core.events/reload-handler! db handler-id)
                :dispatch [:item-handler/request-item! handler-id]}))))
