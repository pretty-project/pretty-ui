
(ns pretty-presets.preset-pool.side-effects
    (:require [pretty-presets.preset-pool.state :as preset-pool.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-preset!
  ; @param (keyword) preset-id
  ; @param (function or map) preset
  ;
  ; @usage
  ; (reg-preset! :my-button {:fill-color  :muted 
  ;                          :hover-color :muted})
  ;
  ; @usage
  ; (reg-preset! :my-button #(assoc % :fill-color  :muted
  ;                                   :hover-color :muted))
  [preset-id preset-props]
  (swap! preset-pool.state/PRESETS assoc preset-id preset-props))
