
(ns pretty-elements.select.subs
    (:require [pretty-elements.input.subs :as input.subs]
              [re-frame.api        :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-option
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (boolean)
  [db [_ select-id {:keys [option-value-f value-path] :as select-props}]]
  (let [selected-value (get-in db value-path)
        options        (r input.subs/get-input-options db select-id select-props)]
       (letfn [(f0 [option] (let [option-value (option-value-f option)]
                                 (if (=  selected-value option-value)
                                     (-> option))))]
              (some f0 options))))

(defn get-selected-option-label
  ; @ignore
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
  ; @ignore
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

; @ignore
(r/reg-sub :pretty-elements.select/get-selected-option-label get-selected-option-label)
