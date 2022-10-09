
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.transfer
    (:require [re-frame.api      :as r]
              [x.server-core.api :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-transfer! :router/transfer-client-routes!
  {:data-f      (fn [_] (r/subscribed [:router/get-client-routes]))
   :target-path [:router :route-handler/client-routes]})
