
(ns elements.label.side-effects
    (:require [elements.label.state :as label.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-info-text-visiblity!
  ; @ignore
  ;
  ; @param (keyword) label-id
  [label-id]
  (swap! label.state/INFO-TEXT-VISIBILITY update label-id not))
