

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.step-handler.prototypes
    (:require [mid-fruits.candy                   :refer [param]]
              [x.app-gestures.step-handler.config :as step-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handler-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) handler-props
  ;  {:autostep? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:autostep? (boolean)
  ;   :infinite-stepping? (boolean)
  ;   :paused? (boolean)
  ;   :step-duration (integer)
  ;   :step-interval (integer)}
  [{:keys [autostep?] :as handler-props}]
  (merge {:autostep?          false
          :infinite-stepping? true
          :step-duration      step-handler.config/DEFAULT-STEP-DURATION}
         (if autostep? {:paused?       false
                        :step-interval step-handler.config/DEFAULT-STEP-INTERVAL})
         (param handler-props)))
