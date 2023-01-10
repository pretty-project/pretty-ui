
(ns elements.plain-field.events
    (:require [elements.input.events  :as input.events]
              [elements.input.helpers :as input.helpers]
              [re-frame.api           :as r :refer [r]]
              [x.db.api               :as x.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r input.events/use-initial-value! db field-id field-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r input.events/clear-value! db field-id field-props))

(defn empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-value-f (function)
  ;  :value-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]}]]
  (let [field-value (field-value-f "")]
       (assoc-in db value-path field-value)))

(defn store-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-value-f (function)
  ;  :value-path (vector)}
  ; @param (string) field-content
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]} field-content]]
  (let [field-value (field-value-f field-content)]
       (if (input.helpers/value-path->vector-item? value-path)
           (r x.db/set-vector-item! db value-path field-value)
           (r x.db/set-item!        db value-path field-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.plain-field/clear-value! clear-value!)
