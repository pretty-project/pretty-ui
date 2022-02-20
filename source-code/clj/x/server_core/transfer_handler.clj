;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v0.8.2
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.transfer-handler
    (:require [server-fruits.http          :as http]
              [x.server-core.engine        :as engine]
              [x.server-core.event-handler :as event-handler]))



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce HANDLERS (atom {}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-transfer-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) transfer-id
  ; @param (map) transfer-props
  [transfer-id transfer-props]
  (swap! HANDLERS assoc transfer-id transfer-props))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-transfer!
  ; @param (keyword)(opt) transfer-id
  ; @param (map) transfer-props
  ;  {:data-f (function)
  ;   :target-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  (a/reg-transfer! {...})
  ;
  ; @usage
  ;  (a/reg-transfer! :my-transfer {...})
  ;
  ; @usage
  ;  (defn my-data-f [request] {:my-data ...})
  ;  (a/reg-transfer! {:data-f my-data-f})
  ([transfer-props]
   (reg-transfer! (engine/id) transfer-props))

  ([transfer-id transfer-props]
   (store-transfer-props! transfer-id transfer-props)))

; @usage
;  [:core/reg-transfer! :my-transfer {...}]
(event-handler/reg-fx :core/reg-transfer! reg-transfer!)



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
         (reduce-kv f {} @HANDLERS)))

(defn download-transfer-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (http/map-wrap {:body (download-transfer-data-f request)}))
