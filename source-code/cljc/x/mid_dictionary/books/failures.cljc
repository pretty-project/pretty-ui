
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.failures)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:failed-to-copy
           {:en "Failed to copy"
            :hu "Sikertelen másolás"}
           :failed-to-delete
           {:en "Failed to delete"
            :hu "Sikertelen törlés"}
           :failed-to-duplicate
           {:en "Failed to duplicate"
            :hu "Sikertelen duplikálás"}
           :failed-to-rename
           {:en "Failed to rename"
            :hu "Sikertelen átnevezés"}
           :failed-to-save
           {:en "Failed to save"
            :hu "Sikertelen mentés"}
           :failed-to-undo-copy
           {:en "Failed to undo copy"
            :hu "A másolás visszavonása sikertelen"}
           :failed-to-undo-delete
           {:en "Failed to undo delete"
            :hu "A törlés visszavonása sikertelen"}
           :failed-to-undo-duplicate
           {:en "Failed to undo duplicate"
            :hu "A duplikálás visszavonása sikertelen"}
           :failed-to-update
           {:en "Failed to update"
            :hu "Sikertelen változtatás"}})
