
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.privacy-policy.effects
    (:require [x.app-core.api                   :as a]
              [x.app-views.privacy-policy.views :as privacy-policy.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-privacy-policy!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:views/render-error-page! :under-construction])
