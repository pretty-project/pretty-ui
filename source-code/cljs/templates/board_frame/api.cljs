
(ns templates.board-frame.api
    (:require [templates.board-frame.core.side-effects]
              [templates.board-frame.core.helpers    :as core.helpers]
              [templates.board-frame.side-menu.views :as side-menu.views]
              [templates.board-frame.wrapper.views   :as wrapper.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.board-frame.core.helpers
(def get-layout       core.helpers/get-layout)
(def layout-selected? core.helpers/layout-selected?)
(def set-layout!      core.helpers/set-layout!)

; templates.board-frame.side-menu.views
(def side-menu side-menu.views/side-menu)

; templates.board-frame.wrapper.views
(def wrapper wrapper.views/wrapper)
