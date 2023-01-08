
(ns elements.input.side-effects
    (:require [elements.input.helpers :as input.helpers]
              [re-frame.api           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.input/mark-input-as-focused! input.helpers/mark-input-as-focused!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.input/unmark-input-as-focused! input.helpers/unmark-input-as-focused!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.input/mark-input-as-visited! input.helpers/mark-input-as-visited!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.input/unmark-input-as-visited! input.helpers/unmark-input-as-visited!)
