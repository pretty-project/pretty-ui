
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
              [x.app-core.api   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn target-query-answers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ query-id server-response]]
  (letfn [(f [db query-key {:pathom/keys [target-value target-path] :as query-answer}]
             (if target-path (assoc-in db target-path target-value)
                             (return   db)))]
         (reduce-kv f db server-response)))
