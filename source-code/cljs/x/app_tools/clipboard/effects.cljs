
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
      {:dispatch [:ui/blow-bubble! ::notification {:body :copied-to-clipboard}]
       :fx       [:tools/copy-to-clipboard! text]}))
