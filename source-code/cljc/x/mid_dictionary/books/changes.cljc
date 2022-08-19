
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.changes)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:changes-discarded
           {:en "Changes discarded"
            :hu "Változások elvetve"}
           :no-changes-to-redo
           {:en "No changes to redo"
            :hu "Nincs megismételhető változás"}
           :no-changes-to-save
           {:en "No changes to save"
            :hu "Nincs elmenthető változás"}
           :no-changes-to-undo
           {:en "No changes to undo"
            :hu "Nincs visszavonható változás"}
           :save-changes!
           {:en "Save changes"
            :hu "Változások mentése"}
           :save-changes?
           {:en "Are you sure you want to save changes?"
            :hu "Biztos vagy benne, hogy szeretnéd elmenteni a változásokat?"}
           :save-n-changes
           {:en " changes to be saved"
            :hu " változtatás mentése"}
           :undo-all-changes?
           {:en "Are you sure you want to undo all changes?"
            :hu "Biztos vagy benne, hogy szeretnéd visszavonni a változásokat?"}
           :undo-changes!
           {:en "Undo changes"
            :hu "Változások visszavonása"}
           :unsaved-changes-discarded
           {:en "Unsaved changes discarded"
            :hu "Nem mentett változások elvetve"}})
