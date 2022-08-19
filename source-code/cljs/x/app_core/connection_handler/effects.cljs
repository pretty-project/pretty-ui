
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.connection-handler.effects
    (:require [x.app-core.event-handler          :as event-handler :refer [r]]
              [x.app-core.lifecycle-handler.subs :as lifecycle-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/connect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler.subs/get-period-events db :on-browser-online)}))

(event-handler/reg-event-fx
  :core/disconnect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler.subs/get-period-events db :on-browser-offline)}))
