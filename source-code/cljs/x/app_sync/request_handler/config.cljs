
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  Mennyi ideig várjon a szerver válaszára
(def DEFAULT-REQUEST-TIMEOUT 15000)

; @constant (ms)
(def DEFAULT-IDLE-TIMEOUT 250)

; @constant (string)
(def INVALID-REQUEST-RESPONSE-ERROR "Invalid request response!")
