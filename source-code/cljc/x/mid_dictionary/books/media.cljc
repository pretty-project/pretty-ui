
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.media)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:create-directory!
           {:en "Create directory"
            :hu "Mappa létrehozása"}
           :delete-directory?
           {:en "Are you sure you want to delete this directory?"
            :hu "Biztos vagy benne, hogy szeretnéd törölni ezt a mappát?"}
           :delete-file?
           {:en "Are you sure you want to delete this file?"
            :hu "Biztos vagy benne, hogy szeretnéd törölni ezt a fájlt?"}
           :deleting-files-and-directories-is-not-reversible
           {:en "Deleting files and directories is not reversible!"
            :hu "A fájlok és mappák törlése nem visszavonható!"}
           :directory-does-not-exists
           {:en "This is not the directory you are looking for"
            :hu "A mappa nem található!"}
           :directory-name
           {:en "Directory name"
            :hu "Mappa neve"}
           :download-file!
           {:en "Download file"
            :hu "Fájl letöltése"}
           :download-image!
           {:en "Download image"
            :hu "Kép letöltése"}
           :drop-files-here-to-upload
           {:en "Drop files here to upload!"
            :hu "Húzd ide a fájlokat, amiket fel szeretnél tölteni!"}
           :empty-directory
           {:en "Empty directory"
            :hu "Üres mappa"}
           :file-upload-failure
           {:en "File upload failure"
            :hu "Sikertelen fájlfeltöltés"}
           :file-uploading-in-progress
           {:en "File uploading in progress"
            :hu "Fájlfeltöltés folyamatban"}
           :file-not-found
           {:en "File not found"
            :hu "A fájl nem található"}
           :filename
           {:en "Filename"
            :hu "Fájlnév"}
           :files-uploaded
           {:en "Files uploaded"
            :hu "Sikeres fájlfeltöltés"}
           :filesize
           {:en "Filesize"
            :hu "Fájlméret"}
           :free-n-space
           {:en "Free %1 %2 space!"
            :hu "Szabadíts fel %1 %2 szabad helyet!"}
           :image-gallery
           {:en "Image gallery"
            :hu "Képgaléria"}
           :invalid-name
           {:en "Invalid name!"
            :hu "Nem megfelelő név!"}
           :new-directory
           {:en "New directory"
            :hu "Új mappa"}
           :no-file-selected
           {:en "No file selected"
            :hu "Nincsen kiválaszott fájl"}
           :no-files-selected
           {:en "No files selected"
            :hu "Nincsenek kiválaszott fájlok"}
           :no-image-selected
           {:en "No image selected"
            :hu "Nincsen kiválasztott kép"}
           :no-images-selected
           {:en "No images selected"
            :hu "Nincsenek kiválasztott képek"}
           :save-file?
           {:en "Are you sure you want to save this file to device?"
            :hu "Biztos vagy benne, hogy szeretnéd menteni ezt a fájlt az eszközre?"}
           :search-in-the-directory
           {:en "Search in the directory"
            :hu "Keresés a mappában"}
           :select-image!
           {:en "Select image"
            :hu "Kép kiválasztása"}
           :select-images!
           {:en "Select images"
            :hu "Képek kiválasztása"}
           :select-the-files-you-would-like-to-attach
           {:en "Select the files you would like to attach!"
            :hu "Válaszd ki a csatolni kívánt fájlokat!"}
           :select-thumbnail!
           {:en "Select thumbnail"
            :hu "Bélyegkép kiválasztása"}
           :set-thumbnail!
           {:en "Set thumbnail"
            :hu "Bélyegkép beállítása"}
           :there-is-not-enough-space
           {:en "There is not enough space available to complete this operation"
            :hu "Nincs elegendő hely a művelet befejezéséhez"}
           :thumbnail
           {:en "Thumbnail"
            :hu "Bélyegkép"}
           :thumbnails
           {:en "Thumbnails"
            :hu "Bélyegképek"}
           :upload-file!
           {:en "Upload file"
            :hu "Fájl feltöltése"}
           :upload-files!
           {:en "Upload files"
            :hu "Fájlok feltöltése"}
           :uploading-file-count
           {:en "Uploading file count"
            :hu "Feltöltésre váró fájlok száma"}
           :uploading-files-size
           {:en "Uploading files size"
            :hu "Feltöltésre váró fájlok mérete"}
           :uploading-n-files...
           {:en "Uploading % files ..."
            :hu "% fájl feltöltése ..."}
           :uploading-n-files-in-progress...
           {:en "Uploading % file(s) in progress ..."
            :hu "% fájl feltöltése folyamatban ..."}})
