
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.transfer-handler.side-effects
    (:require [mid-fruits.random                    :as random]
              [x.server-core.event-handler          :as event-handler]
              [x.server-core.transfer-handler.state :as transfer-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-transfer!
  ; @param (keyword)(opt) transfer-id
  ; @param (map) transfer-props
  ;  {:data-f (function)
  ;   :target-path (vector)}
  ;
  ; @usage
  ;  (a/reg-transfer! {...})
  ;
  ; @usage
  ;  (a/reg-transfer! :my-transfer {...})
  ;
  ; @usage
  ;  (defn my-data-f [request] {:my-data ...})
  ;  (a/reg-transfer! {:data-f      my-data-f
  ;                    :target-path [:my-data]})
  ([transfer-props]
   (reg-transfer! (random/generate-keyword) transfer-props))

  ([transfer-id transfer-props]
   (swap! transfer-handler.state/HANDLERS assoc transfer-id transfer-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/reg-transfer! :my-transfer {...}]
(event-handler/reg-fx :core/reg-transfer! reg-transfer!)
