
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.transfer-handler.routes
    (:require [http.api                      :as http]
              [x.core.transfer-handler.state :as transfer-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-transfer-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  ; A reg-transfer! függvény által regisztrált függvények visszatérési adatait összegyűjti ...
  (letfn [(f [transfer-data transfer-id {:keys [data-f target-path]}]
             (assoc transfer-data transfer-id {:data (data-f request)
                                               :target-path target-path}))]
         (reduce-kv f {} @transfer-handler.state/HANDLERS)))

(defn download-transfer-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (http/map-wrap {:body (download-transfer-data-f request)}))
