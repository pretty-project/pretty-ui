
(ns elements.text-field.subs
    (:require [elements.input.subs :as input.subs]
              [re-frame.api        :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stored-value-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (boolean)
  [db [_ field-id {:keys [validator value-path] :as field-props}]]
  (let [stored-value (get-in db value-path)]
       ((:f validator) stored-value)))

(defn stored-value-invalid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (boolean)
  [db [_ field-id field-props]]
  (let [stored-value-valid? (r stored-value-valid? db field-id field-props)]
       (not stored-value-valid?)))

(defn stored-value-not-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (boolean)
  [db [_ field-id {:keys [field-content-f value-path]}]]
  (let [stored-value (get-in db value-path)]
       (-> stored-value field-content-f str empty?)))
