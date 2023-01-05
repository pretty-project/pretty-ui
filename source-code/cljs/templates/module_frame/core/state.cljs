
(ns templates.module-frame.core.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(def LAYOUT (ratom {}))
