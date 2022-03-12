
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.transfer
    (:require [x.server-core.event-handler    :as event-handler :refer [r]]
              [x.server-core.transfer-handler :as transfer-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler/reg-transfer! :core/transfer-app-config!
                                {:data-f      (fn [_] (event-handler/subscribed [:core/get-app-config]))
                                 :target-path [:core/app-config :data-items]})

(transfer-handler/reg-transfer! :core/transfer-site-config!
                                {:data-f      (fn [_] (event-handler/subscribed [:core/get-site-config]))
                                 :target-path [:core/site-config :data-items]})
