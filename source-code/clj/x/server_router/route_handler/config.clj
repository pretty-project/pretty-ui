
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-JS-BUILD :app)

; @constant (keywords in vector)
(def CLIENT-ROUTE-KEYS [:js-build :client-event :on-leave-event :restricted? :route-parent :route-template])

; @constant (keywords in vector)
(def SERVER-ROUTE-KEYS [:js-build :get :post :restricted? :route-template :server-event])
