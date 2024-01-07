
(ns pretty-inputs.checkbox.subs
    (:require [fruits.vector.api          :as vector]
              [pretty-inputs.input.subs :as input.subs]
              [re-frame.api               :as r :refer [r]]))
 
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-checked?
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:option-value-f (function)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [checkbox-id {:keys [option-value-f] :as checkbox-props} option]
  (let [options      (input.env/get-input-options checkbox-id checkbox-props)
        stored-value (input.env/get-input-value   checkbox-id checkbox-props)
        option-value (option-value-f option)]
       (if (vector/count-min? options 2)
           (vector/contains-item? stored-value option-value)
           (=                     stored-value option-value))))


(defn option-checked?
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:option-value-f (function)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ checkbox-id {:keys [option-value-f] :as checkbox-props} option]]
  ; XXX#7234 (source-code/cljs/pretty_elements/checkbox/events.cljs)
  (let [options      (r input.subs/get-input-options db checkbox-id checkbox-props)
        stored-value (r input.subs/get-input-value   db checkbox-id checkbox-props)
        option-value (option-value-f option)]
       (if (vector/count-min? options 2)
           (vector/contains-item? stored-value option-value)
           (=                     stored-value option-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-sub :pretty-inputs.checkbox/option-checked? option-checked?)
