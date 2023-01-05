
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
  ; Ha a checkbox elem ...
  ; ... több opciót jelenít meg, akkor az egyes kiválaszott opciók értéke
  ;    egy vektorban felsorolva kerül a value-path Re-Frame adatbázis útvonalra.
  ; ... egy opciót jelenít meg, akkor az egy opció esetlegesen kiválasztott
  ;    értéke kerül a value-path Re-Frame adatbázis útvonalra.
  (let [options      (r input.subs/get-input-options db checkbox-id checkbox-props)
        option-value (option-value-f option)]
       (as-> db % (r input.events/mark-as-visited! % checkbox-id)
                  (if (vector/min? options 2)
                      (r x.db/apply-item!        % value-path vector/toggle-item option-value)
                      (r x.db/toggle-item-value! % value-path                    option-value)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.checkbox/checkbox-did-mount checkbox-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.checkbox/toggle-option! toggle-option!)
