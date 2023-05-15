
(ns elements.password-field.side-effects
    (:require [elements.password-field.state :as password-field.state]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-password-visibility!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  (swap! password-field.state/PASSWORD-VISIBILITY update field-id not))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :elements.password-field/toggle-password-visibility! toggle-password-visibility!)
