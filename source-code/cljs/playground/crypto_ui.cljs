
(ns playground.crypto-ui
  (:require [x.app-core.api :as a]))



;; --- Helpers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-attributes
  ; @param (keyword) kline-id
  ; @param (map) kline-props
  ;
  ; @return (map)
  [kline-id {:keys []}]
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
