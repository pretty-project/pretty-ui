
(ns elements.select.events
    (:require [elements.input.events :as input.events]
              [noop.api              :refer [return]]
              [re-frame.api          :refer [r]]
              [vector.api            :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-will-mount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  [db [_ select-id select-props]]
  (as-> db % (r input.events/use-initial-value!   % select-id select-props)
             (r input.events/use-initial-options! % select-id select-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [option-value-f value-path]} option]]
  (let [option-value (option-value-f option)]
       (assoc-in db value-path option-value)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  [db [_ select-id select-props]]
  (r input.events/clear-value! db select-id select-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [options-path]} option]]
  (update-in db options-path vector/cons-item-once option))
