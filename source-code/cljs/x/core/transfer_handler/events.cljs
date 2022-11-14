
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.transfer-handler.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-transfer-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ server-response]]
  (letfn [(f [db transfer-id {:keys [data target-path]}]
             (cond-> db target-path (assoc-in target-path data)))]
         (reduce-kv f db server-response)))
