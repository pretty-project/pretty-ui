
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.debug-handler.helpers
    (:require [mid.x.core.debug-handler.helpers :as helpers]
              [server-fruits.http               :as http]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.core.debug-handler.helpers
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
