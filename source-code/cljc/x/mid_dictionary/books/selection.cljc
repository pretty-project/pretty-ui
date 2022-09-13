
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.selection)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:deselect!
           {:en "Deselect"
            :hu "Kiválasztás megszüntetése"}
           :select
           {:en "Select"
            :hu "Kijelölés"}
           :select!
           {:en "Select"
            :hu "Válassz"}
           :select-all!
           {:en "Select all"
            :hu "Összes kijelölése"}
           :uncheck-selected!
           {:en "Uncheck selected"
            :hu "Kijelölés megszüntetése"}
           :unselect!
           {:en "Unselect"
            :hu "Kijelölés megszűntetése"}})
