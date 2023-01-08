
(ns elements.expandable.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce EXPANDED-ELEMENTS (ratom {}))
