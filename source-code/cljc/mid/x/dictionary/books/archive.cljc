
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.archive)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:archive-selected-items?
           {:en "Are you sure you want to archive selected items?"
            :hu "Biztos vagy benne, hogy szeretnéd archiválni a kiválasztott elemeket?"}
           :archive!
           {:en "Archive"
            :hu "Archiválás"}
           :archived
           {:en "Archived"
            :hu "Archiválva"}
           :archived-items
           {:en "Archived items"
            :hu "Archivált elemek"}
           :item-archived
           {:en "Item archived"
            :hu "Elem archiválva"}
           :item-unarchived
           {:en "Item unarchived"
            :hu "Elem dearchiválva"}
           :items-archived
           {:en "Items archived"
            :hu "Elemek archiválva"}
           :items-unarchived
           {:en "Items unarchived"
            :hu "Elemek dearchiválva"}
           :n-items-archived
           {:en "% item(s) archived"
            :hu "% elem archiválva"}
           :n-items-unarchived
           {:en "% item(s) unarchived"
            :hu "% elem dearchiválva"}
           :unarchive!
           {:en "Unarchive"
            :hu "Dearchiválás"}
           :unarchived
           {:en "Unarchived"
            :hu "Dearchiválva"}})
