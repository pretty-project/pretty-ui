
(ns templates.module-frame.api
    (:require [templates.module-frame.core.side-effects]
              [templates.module-frame.core.helpers    :as core.helpers]
              [templates.module-frame.side-menu.views :as side-menu.views]
              [templates.module-frame.wrapper.views   :as wrapper.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.module-frame.core.helpers
(def get-layout       core.helpers/get-layout)
(def layout-selected? core.helpers/layout-selected?)
(def set-layout!      core.helpers/set-layout!)

; templates.module-frame.side-menu.views
(def side-menu side-menu.views/side-menu)

; templates.module-frame.wrapper.views
(def wrapper wrapper.views/wrapper)
