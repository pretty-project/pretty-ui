
(ns elements.color-selector.events
    (:require [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-color-selector-option!
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:value-path (vector)}
  ; @param (string) option
  [db [_ _ {:keys [value-path]} option]]
  (update-in db value-path vector/toggle-item option))
