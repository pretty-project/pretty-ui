
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.config)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-CORE-JS "app.js")

; @constant (keywords in vector)
(def CLIENT-ROUTE-KEYS [:client-event :core-js :on-leave-event :restricted? :route-template])

; @constant (keywords in vector)
(def SERVER-ROUTE-KEYS [:get :core-js :post :restricted? :route-template :server-event])