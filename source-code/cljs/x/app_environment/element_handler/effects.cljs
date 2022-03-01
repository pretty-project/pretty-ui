
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.element-handler.effects
    (:require [x.app-core.api :as a]))



;; -- Animated actions effect events ------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/remove-element-animated!
  ; @param (integer) timeout
  ; @param (string) element-id
  ;
  ; @usage
  ;  [:environment/remove-element-animated! 500 "my-element"]
  (fn [_ [_ timeout element-id]]
      {:fx             [:environment/set-element-attribute! element-id "data-animation" "hide"]
       :dispatch-later [{:ms timeout :fx [:environment/remove-element! element-id]}]}))

(a/reg-event-fx
  :environment/hide-element-animated!
  ; @param (integer) timeout
  ; @param (string) element-id
  ;
  ; @usage
  ;  [:environment/hide-element-animated! 500 "my-element"]
  (fn [_ [_ timeout element-id]]
      {:fx             [:environment/set-element-attribute! element-id "data-animation" "hide"]
       :dispatch-later [{:ms timeout :fx [:environment/set-element-style-value!  element-id "display" "none"]}
                        {:ms timeout :fx [:environment/remove-element-attribute! element-id "data-animation"]}]}))

(a/reg-event-fx
  :environment/reveal-element-animated!
  ; @param (string) element-id
  ;
  ; @usage
  ;  [:environment/reveal-element-animated! "my-element"]
  (fn [_ [_ element-id]]
      {:fx-n [[:environment/set-element-style-value! element-id "display"        "block"]
              [:environment/set-element-attribute!   element-id "data-animation" "reveal"]]}))
