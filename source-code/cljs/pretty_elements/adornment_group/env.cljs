
(ns pretty-elements.adornment-group.env
    (:require [pretty-engine.api :as pretty-engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-adornment-timeout-left
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ;
  ; @return (ms)
  [adornment-id]
  (pretty-engine/get-element-state adornment-id :timeout-left))
