
(ns tools.infinite-loader.side-effects
    (:require [re-frame.api                :as r]
              [time.api                    :as time]
              [tools.infinite-loader.state :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pause-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ; (pause-loader! :my-loader)
  [loader-id]
  (swap! state/OBSERVERS assoc-in [loader-id :paused?] true))

(defn restart-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ; (restart-loader! :my-loader)
  [loader-id]
  (swap! state/OBSERVERS assoc-in [loader-id :paused?] false))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reload-loader!
  ; WARNING! NON-PUBLIC! DO NOT USE!

  ; @param (keyword) loader-id
  [loader-id]
  ; Placing the observer out of the viewport and restoring it triggers
  ; the callback function.
  ;
  ; If the observer placed out of the viewport for a too short while (e.g. 5ms),
  ; the callback function won't be triggered.
  (pause-loader! loader-id)
  (letfn [(f [] (restart-loader! loader-id))]
         (time/set-timeout! f 50)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:infinite-loader/pause-loader! :my-loader]
(r/reg-fx :infinite-loader/pause-loader! pause-loader!)

; @usage
; [:infinite-loader/restart-loader! :my-loader]
(r/reg-fx :infinite-loader/restart-loader! restart-loader!)

; @usage
; [:infinite-loader/reload-loader! :my-loader]
(r/reg-fx :infinite-loader/reload-loader! reload-loader!)
