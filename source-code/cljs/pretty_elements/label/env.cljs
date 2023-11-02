
(ns pretty-elements.label.env
    (:require [pretty-elements.label.state :as label.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn info-text-visible?
  ; @ignore
  ;
  ; @param (keyword) label-id
  ;
  ; @return (boolean)
  [label-id]
  (get @label.state/INFO-TEXT-VISIBILITY label-id))
