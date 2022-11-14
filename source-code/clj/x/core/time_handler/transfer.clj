
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.time-handler.transfer
    (:require [time.api                             :as time]
              [x.core.transfer-handler.side-effects :as transfer-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler.side-effects/reg-transfer! :x.core/transfer-server-time!
  {:data-f      (fn [_] (time/timestamp-string))
   :target-path [:x.core :time-handler/meta-items :server-time]})
