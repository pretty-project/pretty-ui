
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.register.state)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (vector)
(defonce HANDLERS    (atom {}))

; @atom (map)
(defonce ENVIRONMENT (atom {}))
