
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.transfer
    (:require [re-frame.api :as r]
              [x.core.api   :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-transfer! :x.router/transfer-client-routes!
  {:data-f      (fn [_] (r/subscribed [:x.router/get-client-routes]))
   :target-path [:x.router :route-handler/client-routes]})
