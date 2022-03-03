
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.engine)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  Mennyi ideig várjon a szerver válaszára
(def DEFAULT-REQUEST-TIMEOUT 15000)

; @constant (ms)
(def DEFAULT-IDLE-TIMEOUT 250)

; @constant (string)
(def INVALID-REQUEST-RESPONSE-ERROR "Invalid request response!")



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(def REFERENCES (atom {}))
