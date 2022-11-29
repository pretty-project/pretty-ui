
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.clipboard.side-effects
    (:require [re-frame.api                  :as r]
              [tools.clipboard.helpers       :as helpers]
              [tools.clipboard.views         :as views]
              [tools.temporary-component.api :as temporary-component]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-text!
  ; @usage
  ;  (copy-text! "My text")
  ;
  ; @param (string) text
  [text]
  (temporary-component/append-component! [views/clipboard text] helpers/copy-text-f)
  (temporary-component/remove-component!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:clipboard/copy-text! "My text"]
(r/reg-fx :clipboard/copy-text! copy-text!)
