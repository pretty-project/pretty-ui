
(ns pretty-elements.switch.subs
    (:require [pretty-elements.checkbox.subs :as checkbox.subs]
              [re-frame.api           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-sub :pretty-elements.switch/option-switched? checkbox.subs/option-checked?)
