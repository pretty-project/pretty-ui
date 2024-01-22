
(ns pretty-inputs.chip-group.side-effects
    (:require [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-chip!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) chip-dex
  ; @param (*) chip-value
  ;
  ; @return (map)
  [group-id group-props chip-dex _]
  (let [chips (core.env/get-input-displayed-value group-id group-props)
        chips (vector/remove-nth-item chips chip-dex)]
       (core.side-effects/input-value-changed group-id group-props chips)))
