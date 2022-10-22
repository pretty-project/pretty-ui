
(ns plugins.dnd-kit.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(def SORTABLE-ITEMS (ratom {}))

; @atom (integer)
(def GRABBED-ITEM (ratom nil))
