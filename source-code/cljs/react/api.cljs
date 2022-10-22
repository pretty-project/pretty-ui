
(ns react.api
    (:require ["react"           :as react]
              [react.references  :as references]
              [react.transitions :as transitions]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; react
(def use-effect react/useEffect)
(def use-ref    react/useRef)
(def use-state  react/useState)

; react.references
(def get-reference  references/get-reference)
(def set-reference! references/set-reference!)

; react.transitions
(def transition      transitions/transition)
(def css-transition  transitions/css-transition)
(def mount-animation transitions/mount-animation)
