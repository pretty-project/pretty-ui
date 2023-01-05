
(ns tools.clipboard.api
    (:require [tools.clipboard.effects]
              [tools.clipboard.side-effects :as side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; tools.clipboard.side-effects
(def copy-text! side-effects/copy-text!)
