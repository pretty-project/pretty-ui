
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.uri.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;
; @usage
;  (bybit/PUBLIC-API-ADDRESS)
(def PUBLIC-API-ADDRESS "https://api.bybit.com/v2/public")

; @constant (string)
;
; @usage
;  (bybit/PUBLIC-TEST-API-ADDRESS)
(def PUBLIC-TEST-API-ADDRESS "https://api-testnet.bybit.com/v2/public")

; @constant (string)
;
; @usage
;  (bybit/PRIVATE-API-ADDRESS)
(def PRIVATE-API-ADDRESS "https://api.bybit.com/v2/private")

; @constant (string)
;
; @usage
;  (bybit/PRIVATE-TEST-API-ADDRESS)
(def PRIVATE-TEST-API-ADDRESS "https://api-testnet.bybit.com/v2/private")
