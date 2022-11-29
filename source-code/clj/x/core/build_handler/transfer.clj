
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

(transfer-handler.side-effects/reg-transfer! :x.core/transfer-build-version!
  {:data-f      (fn [_] (r/subscribed [:x.core/get-build-version]))
   :target-path [:x.core :build-handler/meta-items :build-version]})
