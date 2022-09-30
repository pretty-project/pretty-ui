
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
              [x.app-core.api   :as a]))



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
