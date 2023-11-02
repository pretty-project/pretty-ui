
(ns pretty-elements.dropdown-menu.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-menu {:active-dex 42} ...}
(defonce MENUS (ratom {}))
