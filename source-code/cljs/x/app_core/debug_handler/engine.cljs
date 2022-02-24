
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.engine
    (:require [x.mid-core.debug-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.debug-handler.engine
(def query-string->debug-mode engine/query-string->debug-mode)
