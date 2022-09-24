
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.clipboard.effects
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clipboard/copy-text!
  ; @param (string) text
  ;
  ; @usage
  ;  [:clipboard/copy-text! "My text"]
  (fn [_ [_ text]]
      {:dispatch [:ui/render-bubble! ::notification {:body :copied-to-clipboard}]
       :fx       [:clipboard/copy-text! text]}))
