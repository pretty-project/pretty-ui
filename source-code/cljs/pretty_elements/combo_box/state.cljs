
(ns pretty-elements.combo-box.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(defonce OPTION-HIGHLIGHTS (ratom {}))