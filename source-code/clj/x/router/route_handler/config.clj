
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
; XXX#7708 (source-code/clj/x/router/README.md)
(def CACHED-ROUTE-KEYS [:js-build :restricted? :route-template])

; @constant (keywords in vector)
; XXX#7706 (source-code/clj/x/router/README.md)
(def SERVER-ROUTE-KEYS [:js-build :get :post :restricted? :route-template :server-event])

; @constant (keywords in vector)
; XXX#7707 (source-code/clj/x/router/README.md)
(def CLIENT-ROUTE-KEYS [:js-build :client-event :on-leave-event :restricted? :route-parent :route-template])
