
(ns pretty-inputs.chip-group.side-effects
    (:require [fruits.vector.api :as vector]
              [pretty-engine.api :as pretty-engine]))

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
  (let [chips (pretty-engine/get-input-displayed-value group-id group-props)
        chips (vector/remove-nth-item chips chip-dex)]
       (pretty-engine/input-value-changed group-id group-props chips)))
