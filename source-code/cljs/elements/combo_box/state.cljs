
(ns elements.combo-box.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce OPTION-HIGHLIGHTS (ratom {}))
