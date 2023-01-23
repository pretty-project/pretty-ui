
(ns elements.radio-button.events
    (:require [elements.input.events :as input.events]
              [re-frame.api          :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-did-mount
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [db [_ button-id button-props]]
  (as-> db % (r input.events/use-initial-value!   % button-id button-props)
             (r input.events/use-initial-options! % button-id button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ button-id {:keys [option-value-f value-path]} option]]
  (let [option-value (option-value-f option)]
       (assoc-in db value-path option-value)))

(defn clear-value!
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [db [_ button-id button-props]]
  (r input.events/clear-value! db button-id button-props))
