
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.login-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
(def MAX-LOGIN-ATTEMPT-LOG 16)

; @constant (integer)
(def MAX-SUCCESSFUL-LOGIN-LOG 16)

; @constant (integer)
(def MAX-LOGIN-ATTEMPT-ALLOWED 3)

; @constant (s)
(def LOGIN-ATTEMPT-EXPIRATION-WINDOW 300)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def PUBLIC-USER-LOGIN-PROJECTION {:user-login/id 0})
