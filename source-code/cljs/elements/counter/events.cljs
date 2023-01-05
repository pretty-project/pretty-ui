
(ns elements.counter.events
    (:require [candy.api             :refer [return]]
              [elements.counter.subs :as counter.subs]
              [elements.input.events :as input.events]
              [re-frame.api          :as r :refer [r]]
              [x.db.api              :as x.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-box-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [db [_ counter-id counter-props]]
  (r input.events/use-initial-value! db counter-id counter-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn decrease-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {}
  ;
  ; @return (map)
  [db [_ counter-id {:keys [value-path] :as counter-props}]]
  (if (r counter.subs/value-decreasable? db counter-id counter-props)
      (r x.db/apply-item!                db value-path dec)
      (return                            db)))

(defn increase-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {}
  ;
  ; @return (map)
  [db [_ counter-id {:keys [value-path] :as counter-props}]]
  (if (r counter.subs/value-increasable? db counter-id counter-props)
      (r x.db/apply-item!                db value-path inc)
      (return                            db)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.counter/counter-box-did-mount counter-box-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.counter/decrease-value! decrease-value!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.counter/increase-value! increase-value!)
