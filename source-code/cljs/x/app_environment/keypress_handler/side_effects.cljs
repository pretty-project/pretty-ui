
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler.side-effects
    (:require [x.app-core.api                               :as a]
              [x.app-environment.event-handler.side-effects :as event-handler.side-effects]
              [x.app-environment.keypress-handler.config    :as keypress-handler.config]
              [x.app-environment.keypress-handler.state     :as keypress-handler.state]))



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
(a/reg-fx :environment/prevent-keypress-default! prevent-keypress-default!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/enable-keypress-default! enable-keypress-default!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/add-keypress-listeners! add-keypress-listeners!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/remove-keypress-listeners! remove-keypress-listeners!)
