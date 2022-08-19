
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.load-handler.effects
    (:require [x.app-core.event-handler       :as event-handler :refer [r]]
              [x.app-core.load-handler.config :as load-handler.config]
              [x.app-core.load-handler.events :as load-handler.events]
              [x.app-core.load-handler.subs   :as load-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/start-synchron-signal!
  ; @param (keyword) signal-id
  ;
  ; @usage
  ;  [:core/start-synchron-signal! :my-signal]
  (fn [{:keys [db]} [_ signal-id]]
      {:db (r load-handler.events/start-synchron-signal! db signal-id)}))

(event-handler/reg-event-fx
  :core/end-synchron-signal!
  ; @param (keyword) signal-id
  ;
  ; @usage
  ;  [:core/end-synchron-signal! :my-signal]
  (fn [{:keys [db]} [_ signal-id]]
      (if (r load-handler.subs/app-loading? db)
          {:db       (r load-handler.events/end-synchron-signal! db signal-id)
           :dispatch [:core/load-test!]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/load-test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (cond ; Ha a load-handler már nincs {:load-status :loading} állapotban ...
            (not (r load-handler.subs/app-loading? db)) {}
            ; Ha az összes várt jel megérkezett ...
            (r load-handler.subs/all-synchron-signal-ended? db) {:dispatch [:core/app-loaded]}
            ; ...
            (r load-handler.subs/load-timeout-error? db) {:dispatch [:core/load-timeout-reached]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/app-loaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r load-handler.events/set-load-status! db :loaded)
       :fx [:ui/remove-shield!]}))

(event-handler/reg-event-fx
  :core/load-timeout-reached
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r load-handler.events/set-load-status! db :load-timeout-reached)
       :fx [:ui/set-shield! load-handler.config/LOAD-TIMEOUT-ERROR]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/initialize-load-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db             (as-> db % (r load-handler.events/set-load-status!  % :loading)
                                  (r load-handler.events/reg-load-started! %))
       :dispatch-later [{:ms load-handler.config/LOAD-TIMEOUT :dispatch [:core/load-test!]}]}))
