
(ns templates.module-frame.core.side-effects
    (:require [re-frame.api                        :as r]
              [templates.module-frame.core.helpers :as core.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:module-frame/set-layout! :my-module :my-layout]
(r/reg-fx :module-frame/set-layout! core.helpers/set-layout!)
