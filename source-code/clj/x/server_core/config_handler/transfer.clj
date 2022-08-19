
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.transfer
    (:require [x.server-core.event-handler                 :as event-handler :refer [r]]
              [x.server-core.transfer-handler.side-effects :as transfer-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler.side-effects/reg-transfer!
  :core/transfer-app-config!
  {:data-f      (fn [_] (event-handler/subscribed [:core/get-app-config]))
   :target-path [:core :config-handler/app-config]})

(transfer-handler.side-effects/reg-transfer!
  :core/transfer-site-config!
  {:data-f      (fn [_] (event-handler/subscribed [:core/get-site-config]))
   :target-path [:core :config-handler/site-config]})
