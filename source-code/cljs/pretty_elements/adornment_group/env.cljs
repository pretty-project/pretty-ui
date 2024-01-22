
(ns pretty-elements.adornment-group.env
    (:require [pretty-elements.core.env :as core.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-adornment-timeout-left
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ;
  ; @return (ms)
  [adornment-id]
  (core.env/get-element-state adornment-id :timeout-left))
