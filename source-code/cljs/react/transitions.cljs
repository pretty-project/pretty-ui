
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns react.transitions
    (:require [react.config           :as config]
              [react-transition-group :as react-transition-group]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; react-transition-group
(def transition     react-transition-group/Transition)
(def css-transition react-transition-group/CSSTransition)



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
                      :classNames    {:enter  :r-mount   :enterActive  :r-mounting   :enterDone  :r-mounted
                                      :exit   :r-unmount :exitActive   :r-unmounting :exitDone   :r-unmounted
                                      :appear :r-appear  :appearActive :r-appearing  :appearDone :r-appeared}
                      :appear        true
                      :timeout       (or animation-timeout config/DEFAULT-ANIMATION-TIMEOUT)
                      :unmountOnExit true}
                     component])
