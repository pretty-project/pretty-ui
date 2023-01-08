
(ns elements.select.events
    (:require [candy.api             :refer [return]]
              [elements.input.events :as input.events]
              [re-frame.api          :as r :refer [r]]
              [vector.api            :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-will-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [option-value-f value-path]} option]]
  (let [option-value (option-value-f option)]
       (assoc-in db value-path option-value)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [options-path]} option]]
  (update-in db options-path vector/cons-item-once option))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.select/select-will-mount select-will-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.select/clear-value! clear-value!)
