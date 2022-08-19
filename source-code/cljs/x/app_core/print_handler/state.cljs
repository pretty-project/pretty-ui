
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.print-handler.state
    (:require [x.app-core.print-handler.config :as print-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (ms)
(def SEPARATED-AT (atom print-handler.config/APP-STARTED-AT))

; @atom (integer)
(def SEPARATOR-NO (atom 0))
