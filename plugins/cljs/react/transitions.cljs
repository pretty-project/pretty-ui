
(ns react.transitions
    (:require [react-transition-group :as react-transition-group]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (react-transition-group/*)
(def transition     react-transition-group/Transition)
(def css-transition react-transition-group/CSSTransition)

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
                      :classNames    {:enter  :r-mount   :enterActive  :r-mounting   :enterDone  :r-mounted
                                      :exit   :r-unmount :exitActive   :r-unmounting :exitDone   :r-unmounted
                                      :appear :r-appear  :appearActive :r-appearing  :appearDone :r-appeared}
                      :appear        true
                      :timeout       animation-timeout
                      :unmountOnExit true}
                     component])
