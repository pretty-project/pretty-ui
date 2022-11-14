
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.config-handler.transfer
    (:require [re-frame.api                         :as r :refer [r]]
              [x.core.transfer-handler.side-effects :as transfer-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler.side-effects/reg-transfer! :x.core/transfer-app-config!
  {:data-f      (fn [_] (r/subscribed [:x.core/get-app-config]))
   :target-path [:x.core :config-handler/app-config]})
