
(ns pretty-inputs.checkbox.side-effects
    (:require [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-on-change-f
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (function)
  [checkbox-id checkbox-props]
  (fn [%] (r/dispatch [:pretty-inputs.checkbox/toggle-option! checkbox-id checkbox-props %])))
