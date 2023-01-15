
(ns templates.board-frame.core.side-effects
    (:require [re-frame.api                       :as r]
              [templates.board-frame.core.helpers :as core.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:board-frame/set-layout! :my-board :my-layout]
(r/reg-fx :board-frame/set-layout! core.helpers/set-layout!)
