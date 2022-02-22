
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:
; Version: v0.3.8
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.debug-handler.engine
    (:require [server-fruits.http :as http]
              [x.mid-core.debug-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.debug-handler.engine
(def query-string->debug-mode engine/query-string->debug-mode)



;; -- Helpers -----------------------------------------------------------------
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
  (let [query-string (http/request->query-string request)]
       (query-string->debug-mode query-string)))
