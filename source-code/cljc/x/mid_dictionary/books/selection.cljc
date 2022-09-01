
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
           :n-items-selected
           {:en "% item(s) selected"
            :hu "% elem kiválasztva"}
           :no-item-selected
           {:en "No item selected"
            :hu "Nincs kiválasztott elem"}
           :no-items-selected
           {:en "No items selected"
            :hu "Nincsenek kiválasztott elemek"}
           :select
           {:en "Select"
            :hu "Kijelölés"}
           :select!
           {:en "Select"
            :hu "Válassz"}
           :select-all!
           {:en "Select all"
            :hu "Összes kijelölése"}
           :select-all-items!
           {:en "Select all"
            :hu "Összes elem kijelölése"}
           :select-item!
           {:en "Select item"
            :hu "Elem kijelölése"}
           :uncheck-selected!
           {:en "Uncheck selected"
            :hu "Kijelölés megszüntetése"}
           :unselect!
           {:en "Unselect"
            :hu "Kijelölés megszűntetése"}})
