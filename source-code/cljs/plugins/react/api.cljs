
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.react.api
    (:require ["react"                   :as react]
              [plugins.react.references  :as references]
              [plugins.react.transitions :as transitions]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; react
(def use-effect react/useEffect)
(def use-ref    react/useRef)
(def use-state  react/useState)

; plugins.react.references
(def get-reference  references/get-reference)
(def set-reference! references/set-reference!)

; plugins.react.transitions
(def transition      transitions/transition)
(def css-transition  transitions/css-transition)
(def mount-animation transitions/mount-animation)
