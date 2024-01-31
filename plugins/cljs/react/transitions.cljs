
(ns react.transitions
    (:require [react-transition-group]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (react-transition-group/*)
(def transition       react-transition-group/Transition)
(def css-transition   react-transition-group/CSSTransition)
(def transition-group react-transition-group/TransitionGroup)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mount-animation
  ; @param (Reagent component) component
  ; @param (map) options
  ; {:animation-timeout (ms)(opt)
  ;   Default: 350
  ;  :mounted? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; (defn my-component      [] [:div "My component"])
  ; (defn another-component [] [mount-animation {:mounted? true}
  ;                                             [my-component]])
  [{:keys [animation-timeout mounted?] :or {animation-timeout 350}} component]
  [:> css-transition {:in            mounted?
                      ;:key (fruits.random.api/generate-keyword)
                      :classNames    {:enter  :pr-mount   :enterActive  :pr-mounting   :enterDone  :pr-mounted
                                      :exit   :pr-unmount :exitActive   :pr-unmounting :exitDone   :pr-unmounted
                                      :appear :pr-appear  :appearActive :pr-appearing  :appearDone :pr-appeared}
                      :appear        true
                      :timeout       animation-timeout
                      :unmountOnExit true}
                     component])
