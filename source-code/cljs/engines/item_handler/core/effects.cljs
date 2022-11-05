
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
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

(r/reg-event-fx :item-handler/edit-item!
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-handler/edit-item! :my-handler "my-item"]
  (fn [{:keys [db]} [_ handler-id item-id]]
      ; XXX#5575 (engines.item-viewer.core.effects)
      (if-let [route-handled? (r routes.subs/route-handled? db handler-id)]
              (let [edit-route (r routes.subs/get-edit-route db handler-id item-id)]
                   {:dispatch [:router/go-to! edit-route]})
              (if (r body.subs/body-did-mount? db handler-id)
                  {:db       (r core.events/set-item-id! db handler-id item-id)
                   :dispatch [:item-handler/load-handler! handler-id]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/cancel-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      ; A) ...
      ;
      ; B) ...
      (if-let [route-handled? (r routes.subs/route-handled? db handler-id)]
              ; A)
              [:item-handler/go-up! handler-id])))
              ; B)
              ; ...



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
      ; Nem minden esetben érhető el a current-item-id azonosító!
      ; Pl.: Új elem szerkesztése közben az [:item-handler/cancel-item! ...]
      ;      esemény hatására megtörténő [:item-handler/go-up! ...] esemény
      ;      számára nem elérhető az elem azonosítója, mivel az csak az új elem
      ;      mentésekor jön létre!
      (if-let [current-item-id (r core.subs/get-current-item-id db handler-id)]
              (let [item-route (r routes.subs/get-item-route db handler-id current-item-id)]
                   [:router/go-to! item-route])
              (let [base-route (r transfer.subs/get-transfer-item db handler-id :base-route)]
                   [:router/go-to! base-route]))))
