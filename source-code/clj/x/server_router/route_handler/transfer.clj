
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.transfer
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer! :router/transfer-client-routes!
                 {:data-f      (fn [_] (a/subscribed [:router/get-client-routes]))
                  :target-path [:router :route-handler/client-routes]})
