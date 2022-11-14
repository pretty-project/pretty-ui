
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.connection-handler.effects
    (:require [re-frame.api                  :as r :refer [r]]
              [x.core.lifecycle-handler.subs :as lifecycle-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/connect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler.subs/get-period-events db :on-browser-online)}))

(r/reg-event-fx :x.core/disconnect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler.subs/get-period-events db :on-browser-offline)}))
