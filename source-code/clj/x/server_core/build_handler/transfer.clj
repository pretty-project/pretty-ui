
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.build-handler.transfer
    (:require [x.server-core.event-handler    :as event-handler]
              [x.server-core.transfer-handler :as transfer-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler/reg-transfer!
  :core/transfer-app-build!
  {:data-f      (fn [_] (event-handler/subscribed [:core/get-app-build]))
   :target-path [:core/build-handler :meta-items :app-build]})
