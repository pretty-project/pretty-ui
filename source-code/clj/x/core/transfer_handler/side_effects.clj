
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.transfer-handler.side-effects
    (:require [random.api                    :as random]
              [re-frame.api                  :as r]
              [x.core.transfer-handler.state :as transfer-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-transfer!
  ; @param (keyword)(opt) transfer-id
  ; @param (map) transfer-props
  ;  {:data-f (function)
  ;   :target-path (vector)}
  ;
  ; @usage
  ;  (reg-transfer! {...})
  ;
  ; @usage
  ;  (reg-transfer! :my-transfer {...})
  ;
  ; @usage
  ;  (defn my-data-f [request] {:my-data ...})
  ;  (reg-transfer! {:data-f      my-data-f
  ;                  :target-path [:my-data]})
  ([transfer-props]
   (reg-transfer! (random/generate-keyword) transfer-props))

  ([transfer-id transfer-props]
   (swap! transfer-handler.state/HANDLERS assoc transfer-id transfer-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.core/reg-transfer! :my-transfer {...}]
(r/reg-fx :x.core/reg-transfer! reg-transfer!)
