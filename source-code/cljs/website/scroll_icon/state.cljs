
(ns website.scroll-icon.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(def ICON-VISIBLE? (ratom {}))
