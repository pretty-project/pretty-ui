
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.events
    (:require [mid-fruits.candy :refer [return]]
              [mid-fruits.map   :refer [dissoc-in]]
              [re-frame.api     :as r :refer [r]]
              [x.app-sync.api   :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn target-query-answers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (map) server-response
  ;  {:my-answer {:target-path (vector)(opt)}}
  ;
  ; @return (map)
  [db [_ query-id server-response]]
  ; Ha egy szerver-oldali mutation vagy resolver függvény ...
  ; ... visszatérési értéke térkép típus,
  ; ... visszatérési értékében megtalálható a :target-path kulcs,
  ; akkor a target-query-answers! függvény a szerver-oldali függvény válaszát
  ; a válaszban található target-path Re-Frame adatbázis útvonalra írja.
  (letfn [(f [db query-key {:pathom/keys [target-value target-path] :as query-answer}]
             (if target-path (assoc-in db target-path target-value)
                             (return   db)))]
         (reduce-kv f db server-response)))

(defn clear-query-answers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ;
  ; @return (map)
  [db [_ query-id]]
  (let [request-response (r sync/get-request-response db query-id)]
       (letfn [(f [db query-key {:pathom/keys [target-path] :as query-answer}]
                  (if target-path (dissoc-in db target-path)
                                  (return    db)))]
              (reduce-kv f db request-response))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-query-response!
  ; @param (keyword) query-id
  ;
  ; @usage
  ;  (r pathom/clear-query-response! db :my-query)
  ;
  ; @return (map)
  [db [_ query-id]]
  (as-> db % (r clear-query-answers!         % query-id)
             (r sync/clear-request-response! % query-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:pathom/clear-query-response! :my-query]
(r/reg-event-db :pathom/clear-query-response! clear-query-response!)
