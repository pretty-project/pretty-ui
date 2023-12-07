
(ns react.api
    (:require ["react"           :as react]
              [react.references  :as references]
              [react.transitions :as transitions]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (react)
(def use-effect react/useEffect)
(def use-ref    react/useRef)
(def use-state  react/useState)

; @redirect (react.references)
(def get-reference   references/get-reference)
(def set-reference-f references/set-reference-f)

; @redirect (react.transitions)
(def transition      transitions/transition)
(def css-transition  transitions/css-transition)
(def mount-animation transitions/mount-animation)
