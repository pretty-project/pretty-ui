
(ns elements.expandable.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(defonce EXPANDED-ELEMENTS (ratom {}))
