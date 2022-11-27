
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.clipboard.views
    (:require [elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clipboard
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) text
  [text]
  [:input#clipboard {:defaultValue text}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copied-to-clipboard-dialog-body
  [text]
  [:div {:style {:display "flex" :max-width "100%"}}
        [elements/label {:content {:content :copied-to-clipboard-n :replacements [text]}
                         :indent  {:left :s}}]])
