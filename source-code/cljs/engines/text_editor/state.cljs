
(ns engines.text-editor.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce EDITOR-INPUT (ratom {}))

; @atom (map)
(defonce EDITOR-OUTPUT (ratom {}))
