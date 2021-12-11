
(ns playground.crypto-ui
  (:require [x.app-core.api :as a]))



;; --- Helpers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-attributes
  ; @param (keyword) kline-id
  ; @param (map) kline-props
  ;  {}
  ; @param (diagram-props)
  ;  TEMP
  ;  {:highest ()
  ;   :lowest ()
  ;   :range ()}
  ;
  ; @return (map)
  [_ {:keys [open high low close]} {:keys [highest lowest range]}]
  {})



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline
  ; @param (keyword) kline-id
  ; @param (map) kline-props
  ;  {}
  ;
  ; @return (component)
  ([kline-props]
   [kline (a/id) kline-props])

  ([kline-id kline-props]
   (let []
        [:div.crypto--kline (kline-attributes kline-id kline-props)])))
