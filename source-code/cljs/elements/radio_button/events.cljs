
(ns elements.radio-button.events
    (:require [elements.input.events :as input.events]
              [re-frame.api          :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-box-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ button-id {:keys [option-value-f value-path]} option]]
  (let [option-value (option-value-f option)]
       (as-> db % (r input.events/mark-as-visited! % button-id)
                  (assoc-in % value-path option-value))))

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [db [_ button-id button-props]]
  (r input.events/clear-value! db button-id button-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.radio-button/radio-button-box-did-mount radio-button-box-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.radio-button/select-option! select-option!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.radio-button/clear-value! clear-value!)
