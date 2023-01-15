
(ns templates.board-frame.core.helpers
    (:require [templates.board-frame.core.state :as core.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-layout
  ; @param (keyword) board-id
  ;
  ; @usage
  ; (get-layout :my-board)
  ;
  ; @example
  ; (get-layout :my-board)
  ; =>
  ; :my-layout
  ;
  ; @return (keyword)
  [board-id]
  (get-in @core.state/LAYOUT [board-id :layout]))

(defn layout-selected?
  ; @param (keyword) board-id
  ; @param (keyword) layout
  ;
  ; @usage
  ; (layout-selected? :my-board :my-layout)
  ;
  ; @return (boolean)
  [board-id layout]
  (= layout (get-in @core.state/LAYOUT [board-id :layout])))

(defn set-layout!
  ; @param (keyword) board-id
  ; @param (keyword) layout
  ;
  ; @usage
  ; (set-layout! :my-board :my-layout)
  [board-id layout]
  (swap! core.state/LAYOUT assoc-in [board-id :layout] layout))
