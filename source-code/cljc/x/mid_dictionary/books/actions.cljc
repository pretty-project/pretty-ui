
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.actions)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:add!
           {:en "Add"
            :hu "Hozzáadás"}
           :added
           {:en "Added"
            :hu "Hozzáadva"}
           :adding...
           {:en "Adding ..."
            :hu "Hozzáadás ..."}
           :abort!
           {:en "Abort"
            :hu "Megszakítás"}
           :aborted
           {:en "Aborted"
            :hu "Megszakítva"}
           :aborting...
           {:en "Aborting ..."
            :hu "Megszakítás ..."}
           :accept!
           {:en "Accept"
            :hu "Rendben"}
           :accepted
           {:en "Accepted"
            :hu "Elfogadva"}
           :accepting...
           {:en "Accepting ..."
            :hu "Elfogadás ..."}
           :back!
           {:en "Back"
            :hu "Vissza"}
           :cancel!
           {:en "Cancel"
            :hu "Mégsem"}
           :cancelled
           {:en "Cancelled"
            :hu "Visszavonva"}
           :cancelling...
           {:en "Cancelling ..."
            :hu "Visszavonás ..."}
           :close!
           {:en "Close"
            :hu "Bezárás"}
           :closed
           {:en "Closed"
            :hu "Bezárva"}
           :closing...
           {:en "Closing ..."
            :hu "Bezárás ..."}
           :collapse!
           {:en "Hide"
            :hu "Elrejt"}
           :collapsed
           {:en ""
            :hu ""}
           :copy!
           {:en "Copy"
            :hu "Másolás"}
           :copied
           {:en "Copied"
            :hu "Másolva"}
           :copying...
           {:en "Copying ..."
            :hu "Másolás ..."}
           :create!
           {:en "Create"
            :hu "Létrehozás"}
           :created
           {:en "Created"
            :hu "Létrehozva"}
           :creating...
           {:en "Creating ..."
            :hu "Létrehozás ..."}
           :delete!
           {:en "Delete"
            :hu "Törlés"}
           :deleted
           {:en "Deleted"
            :hu "Törölve"}
           :deleting...
           {:en "Deleting ..."
            :hu "Törlés ..."}
           :done!
           {:en "Done"
            :hu "Kész"}
           :duplicate!
           {:en "Duplicate"
            :hu "Duplikálás"}
           :duplicated
           {:en "Duplicated"
            :hu "Duplikálva"}
           :duplicating...
           {:en "Duplicating ..."
            :hu "Duplikálás ..."}
           :exit!
           {:en "Exit"
            :hu "Kilépés"}
           :expand!
           {:en "Expand"
            :hu "Lenyit"}
           :export!
           {:en "Export"
            :hu "Exportálás"}
           :exported
           {:en "Exported"
            :hu "Exportálva"}
           :exporting...
           {:en "Exporting ..."
            :hu "Exportálás ..."}
           :hide!
           {:en "Hide"
            :hu "Elrejt"}
           :hidden
           {:en "Hidden"
            :hu "Rejtett"}
           :import!
           {:en "Import"
            :hu "Importálás"}
           :imported
           {:en "Imported"
            :hu "Importálva"}
           :importing...
           {:en "Importing ..."
            :hu "Importálás ..."}
           :load!
           {:en "Load"
            :hu "Betöltés"}
           :loaded
           {:en "Loaded"
            :hu "Betöltve"}
           :loading...
           {:en "Loading ..."
            :hu "Betöltés ..."}
           :move!
           {:en "Move"
            :hu "Áthelyezés"}
           :moved
           {:en "Moved"
            :hu "Áthelyezve"}
           :moving...
           {:en "Moving ..."
            :hu "Áthelyezés ..."}
           :open!
           {:en "Open"
            :hu "Megnyitás"}
           :opened
           {:en "Opened"
            :hu "Megnyitva"}
           :opening...
           {:en "Opening ..."
            :hu "Megnyitás ..."}
           :paste!
           {:en "Paste"
            :hu "Beillesztés"}
           :pasted
           {:en "Pasted"
            :hu "Beillesztve"}
           :pasting...
           {:en "Pasting ..."
            :hu "Beillesztés ..."}
           :recover!
           {:en "Recover"
            :hu "Visszaállítás"}
           :recovered
           {:en "Recovered"
            :hu "Visszaállítva"}
           :recovering...
           {:en "Recovering ..."
            :hu "Visszaállítás ..."}
           :redo!
           {:en "Redo"
            :hu "Megismétlés"}
           :redid
           {:en "Redid"
            :hu "Megismételve"}
           :redoing...
           {:en "Redoing ..."
            :hu "Megismétlés ..."}
           :refresh!
           {:en "Refresh"
            :hu "Frissítés"}
           :refreshed
           {:en "Refreshed"
            :hu "Frissítve"}
           :refreshing...
           {:en "Refreshing ..."
            :hu "Frissítés ..."}
           :remove!
           {:en "Remove"
            :hu "Eltávolítás"}
           :removed
           {:en "Removed"
            :hu "Eltávolítva"}
           :removing...
           {:en "Removing ..."
            :hu "Eltávolítás ..."}
           :rename!
           {:en "Rename"
            :hu "Átnevezés"}
           :renamed
           {:en "Renamed"
            :hu "Átnevezve"}
           :renaming...
           {:en "Renaming ..."
            :hu "Átnevezés ..."}
           :reset!
           {:en "Reset"
            :hu "Visszaállítás"}
           :reseted
           {:en "Reseted"
            :hu "Visszaállítva"}
           :reseting...
           {:en "Reseting ..."
            :hu "Visszaállítás ..."}
           :restore!
           {:en "Restore"
            :hu "Visszaállítás"}
           :restored
           {:en "Restored"
            :hu "Visszaállítva"}
           :restoring...
           {:en "Recovering ..."
            :hu "Visszaállítás ..."}
           :retry!
           {:en "Retry"
            :hu "Újra"}
           :retried
           {:en ""
            :hu ""}
           :retrying...
           {:en "Retrying ..."
            :hu ""}
           :revert!
           {:en "Revert"
            :hu "Visszaállítás"}
           :reverted
           {:en "Reverted"
            :hu "Visszaállítva"}
           :reverting...
           {:en "Reverting ..."
            :hu "Visszaállítás ..."}
           :save!
           {:en "Save"
            :hu "Mentés"}
           :saved
           {:en "Saved"
            :hu "Sikeres mentés"}
           :saving...
           {:en "Saving ..."
            :hu "Mentés ..."}
           :search!
           {:en "Search"
            :hu "Keresés"}
           :searching...
           {:en "Searching ..."
            :hu "Keresés ..."}
           :select!
           {:en "Select"
            :hu "Válassz"}
           :selected
           {:en "Selected"
            :hu "Kiválasztva"}
           :selecting...
           {:en "Selecting ..."
            :hu "Kiválasztás ..."}
           :share!
           {:en "Share"
            :hu "Megosztás"}
           :shared
           {:en "Shared"
            :hu "Megosztva"}
           :sharing...
           {:en "Sharing ..."
            :hu "Megosztás ..."}
           :undo!
           {:en "Undo"
            :hu "Visszavonás"}
           :undid
           {:en "Undid"
            :hu "Visszavonva"}
           :undoing...
           {:en "Undoing ..."
            :hu "Visszavonás ..."}})
