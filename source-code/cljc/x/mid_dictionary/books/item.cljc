
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:add-item!
           {:en "Add item"
            :hu "Elem hozzáadása"}
           :all-items
           {:en "All items"
            :hu "Összes elem"}
           :create-item!
           {:en "Create item"
            :hu "Elem létrehozása"}
           :delete-item!
           {:en "Delete item"
            :hu "Elem törlése"}
           :duplicate-item!
           {:en "Duplicate item"
            :hu "Elem duplikálása"}
           :delete-selected-items!
           {:en "Delete selected items"
            :hu "Kiválasztott elemek törlése"}
           :delete-selected-items?
           {:en "Are you sure you want to delete selected items?"
            :hu "Biztos vagy benne, hogy szeretnéd törölni a kiválasztott elemeket?"}
           :duplicate-selected-items!
           {:en "Duplicate selected items"
            :hu "Kiválaszott elemek duplikálása"}
           :duplicate-selected-items?
           {:en "Are you sure you want to duplicate selected items?"
            :hu "Biztos vagy benne, hogy szeretnéd másolni a kiválasztott elemeket?"}
           :item-added
           {:en "Item added"
            :hu "Elem hozzáadva"}
           :item-deleted
           {:en "Item deleted"
            :hu "Elem törölve"}
           :item-does-not-exists
           {:en "This item does not exists"
            :hu "Ez az elem még nem létezik"}
           :item-duplicated
           {:en "Item duplicated"
            :hu "Elem duplikálva"}
           :item-removed
           {:en "Item removed"
            :hu "Elem törölve"}
           :n-items
           {:en "% items"
            :hu "% elem"}
           :n-items-deleted
           {:en "% item(s) deleted"
            :hu "% elem törölve"}
           :n-items-duplicated
           {:en "% item(s) duplicated"
            :hu "% elem duplikálva"}
           :n-items-selected
           {:en "% item(s) selected"
            :hu "% elem kiválasztva"}
           :no-item-selected
           {:en "No item selected"
            :hu "Nincs kiválasztott elem"}
           :no-items-selected
           {:en "No items selected"
            :hu "Nincsenek kiválasztott elemek"}
           :no-items-to-show
           {:en "No items to show"
            :hu "Nincsenek megjeleníthető elemek"}
           :remove-item!
           {:en "Remove item"
            :hu "Elem eltávolítása"}
           :remove-item?
           {:en "Are you sure you want to remove this item?"
            :hu "Biztos vagy benne, hogy szeretnéd eltávolítani ezt az elemet?"}
           :select-all-items!
           {:en "Select all"
            :hu "Összes elem kijelölése"}
           :select-item!
           {:en "Select item"
            :hu "Elem kijelölése"}
           :unnamed-item
           {:en "Unnamed item"
            :hu "Névtelen elem"}})
