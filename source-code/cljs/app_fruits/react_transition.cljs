
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.30
; Description:
; Version: v0.2.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.react-transition
    (:require [react-transition-group :as react-transition-group]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; react-transition-group
(def transition     react-transition-group/Transition)
(def css-transition react-transition-group/CSSTransition)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def DEFAULT-ANIMATION-TIMEOUT 500)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mount-animation
  ; @param (component) component
  ; @param (map) options
  ;  {:animation-timeout (ms)(opt)
  ;    Default: DEFAULT-ANIMATION-TIMEOUT
  ;   :mounted? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  (defn my-component   [] [:div "My component"])
  ;  (defn your-component [] [react-transition/mount-animation {:mounted? true}
  ;                                                            [my-component]])
  ;
  ; @return (component)
  [{:keys [animation-timeout mounted?]} component]
  [:> css-transition {:in            mounted?
                      :classNames    {:enter  "r-mount"   :enterActive  "r-mounting"   :enterDone  "r-mounted"
                                      :exit   "r-unmount" :exitActive   "r-unmounting" :exitDone   "r-unmounted"
                                      :appear "r-appear"  :appearActive "r-appearing"  :appearDone "r-appeared"}
                      :appear        true
                      :timeout       (or animation-timeout DEFAULT-ANIMATION-TIMEOUT)
                      :unmountOnExit true}
                     component])
