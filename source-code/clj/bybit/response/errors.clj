
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.response.errors
    (:require [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn response->error?
  ; @param (map) response
  ;  {:body (string)}
  ;
  ; @usage
  ;  (bybit/response->error? {:body "..."})
  ;
  ; @return (boolean)
  [{:keys [body]}]
  (not (string/contains-part? body "\"ret_code\":0,\"ret_msg\":\"OK\"")))

(defn response->invalid-api-details?
  ; @param (map) response
  ;  {:body (string)}
  ;
  ; @usage
  ;  (bybit/response->invalid-api-details? {:body "..."})
  ;
  ; @return (boolean)
  [{:keys [body]}]
  (or (string/contains-part? body "\"ret_code\":10003")
      (string/contains-part? body "\"ret_code\":10004")))
