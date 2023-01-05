
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
  ; @param (string) text
  [text]
  [:div {:style {:display "flex" :max-width "100%"}}
        [elements/label {:content {:content :copied-to-clipboard-n :replacements [text]}
                         :outdent {:left :s}}]])
