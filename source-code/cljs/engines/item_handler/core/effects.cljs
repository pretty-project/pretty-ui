
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.core.effects
    (:require [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.core.events   :as core.events]
              [engines.item-handler.routes.subs   :as routes.subs]
              [engines.item-handler.transfer.subs :as transfer.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/handle-item!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-handler/handle-item! :my-handler "my-item"]
  (fn [{:keys [db]} [_ handler-id item-id]]
      ; XXX#5575
      ; (A) Ha az item-handler engine útvonal-vezérelt, ...
      ;     ... akkor elkészíti az elem megtekintéséhez az útvonalat és átirányít arra.
      ;
      ; (B) Ha az item-handler engine NEM útvonal-vezérelt és a body komponens a React-fába
      ;     van csatolva, ...
      ;     ... akkor beállítja az item-id paraméter értékét az aktuálisan megtekintett
      ;         elem azonosítójaként.
      ;     ... meghívja az [:item-handler/load-handler! ...] eseményt.
      (if-let [route-handled? (r routes.subs/route-handled? db handler-id)]
              (let [item-route (r routes.subs/get-item-route db handler-id item-id)]
                   {:dispatch [:x.router/go-to! item-route]})
              (if (r body.subs/body-did-mount? db handler-id)
                  {:db       (r core.events/set-item-id! db handler-id item-id)
                   :dispatch [:item-handler/load-handler! handler-id]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/load-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      {:db       (r core.events/load-handler! db handler-id)
       :dispatch [:item-handler/request-item! handler-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      (let [base-route (r transfer.subs/get-transfer-item db handler-id :base-route)]
           [:x.router/go-to! base-route])))
