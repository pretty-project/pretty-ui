
(ns elements.select.subs
    (:require [candy.api           :refer [return]]
              [elements.input.subs :as input.subs]
              [re-frame.api        :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-value-f (function)
  ;  :value-path (vector)}
  ;
  ; @return (boolean)
  [db [_ select-id {:keys [option-value-f value-path] :as select-props}]]
  (let [selected-value (get-in db value-path)
        options        (r input.subs/get-input-options db select-id select-props)]
       (letfn [(f [option] (let [option-value (option-value-f option)]
                                (if (= selected-value option-value)
                                    (return option))))]
              (some f options))))

(defn get-selected-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-label-f (function)}
  ;
  ; @return (boolean)
  [db [_ select-id {:keys [option-label-f] :as select-props}]]
  (if-let [selected-option (r get-selected-option db select-id select-props)]
          (option-label-f selected-option)))

(defn stored-value-not-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ;
  ; @return (boolean)
  [db [_ select-id {:keys [value-path]}]]
  (let [stored-value (get-in db value-path)]
       (or (and (seqable? stored-value)
                (empty?   stored-value))
           (nil? stored-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.select/get-selected-option-label get-selected-option-label)
