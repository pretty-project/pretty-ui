
(ns pretty-inputs.chip-group.adornments
    (:require [pretty-inputs.chip-group.side-effects :as chip-group.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-chip-adornment
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) chip-dex
  ; @param (*) chip-value
  ;
  ; @return (map)
  [group-id group-props chip-dex chip-value]
  (let [on-click-f #(chip-group.side-effects/delete-chip! group-id group-props chip-dex chip-value)]
       {:icon       :close
        :on-click-f on-click-f}))
