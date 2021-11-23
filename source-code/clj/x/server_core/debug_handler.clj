
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:
; Version: v0.2.8
; Compatibility: x4.4.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.debug-handler
    (:require [server-fruits.http       :as http]
              [x.mid-core.debug-handler :as debug-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def query-string->debug-mode? debug-handler/query-string->debug-mode?)
(def query-string->debug-mode  debug-handler/query-string->debug-mode)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->debug-mode?
  ; @param (map) request
  ;  {:query-string (string)}
  ;
  ; @return (boolean)
  [request]
  (let [query-string (http/request->query-string request)]
       (query-string->debug-mode? query-string)))

(defn request->debug-mode
  ; @param (map) request
  ;  {:query-string (string)}
  ;
  ; @return (string)
  [request]
  (let [query-string (http/request->query-string request)]
       (query-string->debug-mode query-string)))
