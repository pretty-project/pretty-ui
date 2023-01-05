
(ns elements.switch.subs
    (:require [elements.checkbox.subs :as checkbox.subs]
              [re-frame.api           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.switch/option-switched? checkbox.subs/option-checked?)
