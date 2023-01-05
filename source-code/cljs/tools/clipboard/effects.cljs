
(ns tools.clipboard.effects
    (:require [re-frame.api          :as r]
              [tools.clipboard.views :as views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :clipboard/copy-text!
  ; @param (string) text
  ;
  ; @usage
  ; [:clipboard/copy-text! "My text"]
  (fn [_ [_ text]]
      {:dispatch [:x.ui/render-bubble! ::notification {:body [views/copied-to-clipboard-dialog-body text]}]
       :fx       [:clipboard/copy-text! text]}))
