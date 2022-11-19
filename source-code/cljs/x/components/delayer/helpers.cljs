
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.components.delayer.helpers
    (:require [plugins.reagent.api        :refer [ratom]]
              [time.api                   :as time]
              [x.components.delayer.state :as delayer.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delayer-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) delayer-id
  ; @param (map) delayer-props
  ;  {:timeout (ms)}
  [delayer-id {:keys [timeout]}]
  (letfn [(f [] (swap! delayer.state/DELAYERS assoc-in [delayer-id :time-elapsed?] true))]
         (time/set-timeout! f timeout)))

(defn delayer-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) delayer-id
  ; @param (map) delayer-props
  ;  {:timeout (ms)}
  [delayer-id {:keys [timeout]}]
  (swap! delayer.state/DELAYERS dissoc delayer-id))
