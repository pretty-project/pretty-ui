
(ns pretty-inputs.switch.subs
    (:require [pretty-inputs.checkbox.subs :as checkbox.subs]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-sub :pretty-inputs.switch/option-switched? checkbox.subs/option-checked?)
