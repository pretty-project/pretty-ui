
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.debug-handler.helpers
    (:require [server-fruits.http               :as http]
              [x.mid-core.debug-handler.helpers :as helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.debug-handler.helpers
(def query-string->debug-mode helpers/query-string->debug-mode)



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