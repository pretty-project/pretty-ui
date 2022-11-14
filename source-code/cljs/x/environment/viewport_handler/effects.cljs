
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.viewport-handler.effects
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.environment/viewport-resized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:fx-n [[:x.environment/detect-viewport-profile!]
          [:x.environment/update-viewport-data!]]})
