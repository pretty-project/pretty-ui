
(ns elements.radio-button.subs
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:option-value-f (function)
  ;  :value-path (vector)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ _ {:keys [option-value-f value-path]} option]]
  (let [stored-value     (get-in db value-path)
        option-value     (option-value-f option)]
       (= stored-value option-value)))

(defn any-option-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:value-path (vector)}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [value-path]}]]
  (let [value (get-in db value-path)]
       (-> value nil? not)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.radio-button/option-selected? option-selected?)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.radio-button/any-option-selected? any-option-selected?)
