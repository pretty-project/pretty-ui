
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.load-handler.effects
    (:require [re-frame.api               :as r :refer [r]]
              [x.core.load-handler.config :as load-handler.config]
              [x.core.load-handler.events :as load-handler.events]
              [x.core.load-handler.subs   :as load-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/start-synchron-signal!
  ; @param (keyword) signal-id
  ;
  ; @usage
  ;  [:x.core/start-synchron-signal! :my-signal]
  (fn [{:keys [db]} [_ signal-id]]
      {:db (r load-handler.events/start-synchron-signal! db signal-id)}))

(r/reg-event-fx :x.core/end-synchron-signal!
  ; @param (keyword) signal-id
  ;
  ; @usage
  ;  [:x.core/end-synchron-signal! :my-signal]
  (fn [{:keys [db]} [_ signal-id]]
      (if (r load-handler.subs/app-loading? db)
          {:db       (r load-handler.events/end-synchron-signal! db signal-id)
           :dispatch [:x.core/load-test!]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/load-test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (cond ; Ha a load-handler már nincs {:load-status :loading} állapotban ...
            (not (r load-handler.subs/app-loading? db)) {}
            ; Ha az összes várt jel megérkezett ...
            (r load-handler.subs/all-synchron-signal-ended? db) {:dispatch [:x.core/app-loaded]}
            ; ...
            (r load-handler.subs/load-timeout-error? db) {:dispatch [:x.core/load-timeout-reached]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/app-loaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r load-handler.events/set-load-status! db :loaded)
       :fx [:x.ui/hide-loading-screen!]}))

(r/reg-event-fx :x.core/load-timeout-reached
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r load-handler.events/set-load-status! db :load-timeout-reached)
       :fx [:x.ui/set-error-shield! load-handler.config/LOAD-TIMEOUT-ERROR]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/initialize-load-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db             (as-> db % (r load-handler.events/set-load-status!  % :loading)
                                  (r load-handler.events/reg-load-started! %))
       :dispatch-later [{:ms load-handler.config/LOAD-TIMEOUT :dispatch [:x.core/load-test!]}]}))
