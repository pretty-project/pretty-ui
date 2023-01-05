
(ns tools.infinite-loader.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def OBSERVERS (ratom {}))
