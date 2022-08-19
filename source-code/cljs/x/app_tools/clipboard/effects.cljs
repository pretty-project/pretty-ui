
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard.effects
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/copy-to-clipboard!
  ; @param (string) text
  ;
  ; @usage
  ;  [:tools/copy-to-clipboard! "My text"]
  (fn [_ [_ text]]
      {:dispatch [:ui/render-bubble! ::notification {:body :copied-to-clipboard}]
       :fx       [:tools/copy-to-clipboard! text]}))
