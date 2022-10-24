
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.debug-handler.helpers
    (:require [server-fruits.http               :as http]
              [x.mid-core.debug-handler.helpers :as helpers]))



;; ----------------------------------------------------------------------------
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
  ;  (request->debug-mode {...})
  ;
  ; @return (string)
  [request]
  (-> request http/request->query-string query-string->debug-mode))
