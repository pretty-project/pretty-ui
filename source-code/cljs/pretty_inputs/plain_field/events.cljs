
(ns pretty-inputs.plain-field.events
    (:require [pretty-inputs.input.events :as input.events]
              [re-frame.api               :as r :refer [r]]
              [re-frame.db.api            :as r.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r input.events/clear-value! db field-id field-props))

(defn empty-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]}]]
  (let [field-value (field-value-f "")]
       (assoc-in db value-path field-value)))

(defn store-value!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ; @param (string) field-content
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]} field-content]]
  (let [field-value (field-value-f field-content)]
       (r r.db/set-item! db value-path field-value)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-event-db :pretty-inputs.plain-field/clear-value! clear-value!)
