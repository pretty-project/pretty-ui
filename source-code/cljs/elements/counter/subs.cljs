
(ns elements.counter.subs)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-decreasable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (boolean)
  [db [_ _ {:keys [min-value value-path]}]]
  (let [value (get-in db value-path)]
       (or (nil? min-value)
           (and (some? min-value)
                (<     min-value value)))))

(defn value-increasable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (boolean)
  [db [_ _ {:keys [max-value value-path]}]]
  (let [value (get-in db value-path)]
       (or (nil? max-value)
           (and (some? max-value)
                (>     max-value value)))))
