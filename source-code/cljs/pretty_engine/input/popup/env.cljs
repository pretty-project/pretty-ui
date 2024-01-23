
(ns pretty-engine.input.popup.env
    (:require [pretty-engine.input.state.env :as input.state.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-popup-rendered?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id _]
  (input.state.env/get-input-state input-id :popup-rendered?))
