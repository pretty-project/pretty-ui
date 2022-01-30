
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.backend.router.default-routes
    (:require [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [project-emulator.backend.ui.api :as ui]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (function)
;  No method matched
(def METHOD-NOT-ALLOWED #(http/html-wrap {:body (ui/main %) :status 404}))

; @constant (function)
;  Handler returned nil
(def NOT-ACCEPTABLE     #(http/html-wrap {:body (ui/main %) :status 404}))

; @constant (function)
;  No route matched – {:status 200} handled at client-side
(def NOT-FOUND          #(http/html-wrap {:body (ui/main %) :status 200}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-init {:dispatch-n [[:router/set-default-route! :method-not-allowed METHOD-NOT-ALLOWED]
                                 [:router/set-default-route! :not-acceptable     NOT-ACCEPTABLE]
                                 [:router/set-default-route! :not-found          NOT-FOUND]]}})
