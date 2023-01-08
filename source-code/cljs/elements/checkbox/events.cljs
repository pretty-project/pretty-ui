
(ns elements.checkbox.events
    (:require [elements.input.events :as input.events]
              [elements.input.subs   :as input.subs]
              [re-frame.api          :as r :refer [r]]
              [vector.api            :as vector]
              [x.db.api              :as x.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  [db [_ checkbox-id checkbox-props]]
  (as-> db % (r input.events/use-initial-value!   % checkbox-id checkbox-props)
             (r input.events/use-initial-options! % checkbox-id checkbox-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:option-value-f (function)
  ;  :value-path (vector)}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ checkbox-id {:keys [option-value-f value-path] :as checkbox-props} option]]
  ; XXX#7234
  ; If a checkbox element ...
  ; ... displays more than one option, then the selected options' values
  ;     will be listed in a vector (and stored in the application state).
  ;     {:my-value ["Selected option #1" "Selected option #2"]}
  ; ... displays only one option, then the selected option's value
  ;     will be stored as a single value (in the application state).
  ;     {:my-value "Selected option"}
  (let [options      (r input.subs/get-input-options db checkbox-id checkbox-props)
        option-value (option-value-f option)]
       (if (vector/min? options 2)
           (r x.db/apply-item!        db value-path vector/toggle-item option-value)
           (r x.db/toggle-item-value! db value-path                    option-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.checkbox/checkbox-did-mount checkbox-did-mount)
