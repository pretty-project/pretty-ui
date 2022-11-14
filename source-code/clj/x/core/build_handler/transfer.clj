
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.build-handler.transfer
    (:require [re-frame.api                         :as r]
              [x.core.transfer-handler.side-effects :as transfer-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler.side-effects/reg-transfer! :x.core/transfer-app-build!
  {:data-f      (fn [_] (r/subscribed [:x.core/get-app-build]))
   :target-path [:x.core/build-handler :meta-items :app-build]})
