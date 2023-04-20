
(ns elements.multi-field.subs
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn max-input-count-reached?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:max-input-count (integer)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [max-input-count value-path]}]]
  (let [group-value (get-in db value-path)
        input-count (count group-value)]
       (>= input-count max-input-count)))

(defn get-group-value
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @return (strings in vector)
  [db [_ _ {:keys [value-path]}]]
  (let [group-value (get-in db value-path)]
       (if (empty? group-value)
           [nil] group-value)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-sub :elements.multi-field/get-group-value get-group-value)
