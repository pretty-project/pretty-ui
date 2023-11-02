
(ns pretty-elements.radio-button.subs
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-selected?
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:option-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ _ {:keys [option-value-f value-path]} option]]
  (let [stored-value     (get-in db value-path)
        option-value     (option-value-f option)]
       (= stored-value option-value)))

(defn any-option-selected?
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [value-path]}]]
  (let [value (get-in db value-path)]
       (-> value nil? not)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-sub :pretty-elements.radio-button/option-selected? option-selected?)

; @ignore
(r/reg-sub :pretty-elements.radio-button/any-option-selected? any-option-selected?)
