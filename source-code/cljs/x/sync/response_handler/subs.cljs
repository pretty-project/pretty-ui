
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.sync.response-handler.subs
    (:require [mixed.api    :as mixed]
              [re-frame.api :as r]
              [reader.api   :as reader]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-response
  ; @param (keyword) request-id
  ;
  ; @return (*)
  [db [_ request-id]]
  (get-in db [:x.sync :response-handler/data-items request-id]))

(defn request-response-invalid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:validator-f (function)(opt)}
  ; @param (string) server-response-body
  ;  "{...}"
  ;
  ; @return (boolean)
  [db [_ _ {:keys [validator-f]} server-response-body]]
  ; A sikeres HTTP státusz-kódtól függetlenül ha a szerver-válasz a validator-f
  ; függvény szerint nem megfelelő, tehát a validator-f függvény visszatérési
  ; értéke boolean értékként kiértékelve FALSE, akkor az on-success esemény
  ; helyett az on-failure esemény fog megtörténni ...
  (if validator-f (-> server-response-body reader/string->mixed validator-f not)))

(defn get-invalid-server-response
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:validator-f (function)}
  ; @param (string) server-response-body
  ;  "{...}"
  ;
  ; @return (map)
  ;  {:failure (keyword)
  ;   :response (string)}
  [db [_ _ _ server-response-body]]
  {:failure  :invalid
   :response server-response-body})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.sync/get-request-response :my-request]
(r/reg-sub :x.sync/get-request-response get-request-response)