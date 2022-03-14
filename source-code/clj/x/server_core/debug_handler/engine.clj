
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.debug-handler.engine
    (:require [server-fruits.http              :as http]
              [x.mid-core.debug-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.debug-handler.engine
(def query-string->debug-mode engine/query-string->debug-mode)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->debug-mode
  ; @param (map) request
  ;  {:query-string (string)}
  ;
  ; @usage
  ;  (a/request->debug-mode {...})
  ;
  ; @return (string)
  [request]
  (-> request http/request->query-string query-string->debug-mode))
