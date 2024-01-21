
(ns pretty-elements.label.side-effects
    (:require [pretty-elements.label.state :as label.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-info-text-visibility!
  ; @ignore
  ;
  ; @param (keyword) label-id
  [label-id]
  (swap! label.state/INFO-TEXT-VISIBILITY update label-id not))
