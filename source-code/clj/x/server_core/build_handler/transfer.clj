
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.build-handler.transfer
    (:require [x.server-core.event-handler                 :as event-handler]
              [x.server-core.transfer-handler.side-effects :as transfer-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler.side-effects/reg-transfer!
  :core/transfer-app-build!
  {:data-f      (fn [_] (event-handler/subscribed [:core/get-app-build]))
   :target-path [:core/build-handler :meta-items :app-build]})
