
(ns pretty-inputs.color-selector.events
    (:require [fruits.vector.api          :as vector]
              [pretty-inputs.input.events :as input.events]
              [re-frame.api               :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn options-did-mount
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  [db [_ selector-id selector-props]]
  (as-> db % (r input.events/use-initial-value!   % selector-id selector-props)
             (r input.events/use-initial-options! % selector-id selector-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-color-selector-option!
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:value-path (Re-Frame path vector)}
  ; @param (string) option
  [db [_ _ {:keys [value-path]} option]]
  (update-in db value-path vector/toggle-item option))
