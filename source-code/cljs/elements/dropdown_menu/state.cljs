
(ns elements.dropdown-menu.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-menu {:active-dex 42} ...}
(defonce MENUS (ratom {}))
