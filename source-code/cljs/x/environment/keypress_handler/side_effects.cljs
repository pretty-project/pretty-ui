
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.keypress-handler.side-effects
    (:require [re-frame.api                             :as r]
              [x.environment.event-handler.side-effects :as event-handler.side-effects]
              [x.environment.keypress-handler.config    :as keypress-handler.config]
              [x.environment.keypress-handler.state     :as keypress-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn prevent-keypress-default!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  [key-code]
  (swap! keypress-handler.state/PREVENTED-KEYS assoc key-code true))

(defn enable-keypress-default!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  [key-code]
  (swap! keypress-handler.state/PREVENTED-KEYS dissoc key-code))

(defn add-keypress-listeners!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (event-handler.side-effects/add-event-listener! "keydown" keypress-handler.config/KEYDOWN-LISTENER)
  (event-handler.side-effects/add-event-listener! "keyup"   keypress-handler.config/KEYUP-LISTENER))

(defn remove-keypress-listeners!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (event-handler.side-effects/remove-event-listener! "keydown" keypress-handler.config/KEYDOWN-LISTENER)
  (event-handler.side-effects/remove-event-listener! "keyup"   keypress-handler.config/KEYUP-LISTENER))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/prevent-keypress-default! prevent-keypress-default!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/enable-keypress-default! enable-keypress-default!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/add-keypress-listeners! add-keypress-listeners!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/remove-keypress-listeners! remove-keypress-listeners!)
