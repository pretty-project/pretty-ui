
(ns tools.clipboard.views
    (:require [components.api :as components]))

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

(defn copied-to-clipboard-dialog
  ; @param (string) text
  [text]
  [components/notification-bubble :clipboard/copied-to-clipboard-dialog
                                  {:content {:content :copied-to-clipboard-n :replacements [text]}}])
  
