
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.media)



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
           :directories
           {:en "Directories"
            :hu "Mappák"}
           :directory
           {:en "Directory"
            :hu "Mappa"}
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
           :download-preview!
           {:en "Download preview"
            :hu "Előnézet letöltése"}
           :downloaded
           {:en "Downloaded"
            :hu "Letöltve"}
           :downloaded-at-n
           {:en "Downloaded at: %"
            :hu "Letöltve: %"}
           :drop-files-here-to-upload
           {:en "Drop files here to upload!"
            :hu "Húzd ide a fájlokat, amiket fel szeretnél tölteni!"}
           :empty-directory
           {:en "Empty directory"
            :hu "Üres mappa"}
           :file
           {:en "File"
            :hu "Fájl"}
           :files
           {:en "Files"
            :hu "Fájlok"}
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
           :image
           {:en "Image"
            :hu "Kép"}
           :images
           {:en "Images"
            :hu "Képek"}
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
            :hu "Nincs fájl kiválasztva"}
           :no-files-selected
           {:en "No files selected"
            :hu "Nincsenek fájlok kiválasztva"}
           :no-image-selected
           {:en "No image selected"
            :hu "Nincs kép kiválasztva"}
           :no-images-selected
           {:en "No images selected"
            :hu "Nincsenek képek kiválasztva"}
           :no-thumbnail-selected
           {:en "No thumbnail selected"
            :hu "Nincs bélyegkép kiválasztva"}
           :preview-image
           {:en "Preview image"
            :hu "Előnézeti kép"}
           :refresh-preview!
           {:en "Refresh preview"
            :hu "Előnézet frissítése"}
           :recommended-image-size-n
           {:en "Recommended image size: %1x%2px"
            :hu "Ajánlott képméret: %1x%2px"}
           :save-file?
           {:en "Are you sure you want to save this file to device?"
            :hu "Biztos vagy benne, hogy szeretnéd menteni ezt a fájlt az eszközre?"}
           :search-in-the-directory
           {:en "Search in the directory"
            :hu "Keresés a mappában"}
           :select-file!
           {:en "Select file"
            :hu "Fájl kiválasztása"}
           :select-files!
           {:en "Select files"
            :hu "Fájlok kiválasztása"}
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
           :show-preview!
           {:en "Show preview"
            :hu "Előnézet megtekintése"}
           :there-is-not-enough-space
           {:en "There is not enough space available to complete this operation"
            :hu "Nincs elegendő hely a művelet befejezéséhez"}
           :thumbnail
           {:en "Thumbnail"
            :hu "Bélyegkép"}
           :thumbnails
           {:en "Thumbnails"
            :hu "Bélyegképek"}
           :unique-filename
           {:en "Unique filename"
            :hu "Egyedi fájlnév"}
           :upload-file!
           {:en "Upload file"
            :hu "Fájl feltöltése"}
           :upload-files!
           {:en "Upload files"
            :hu "Fájlok feltöltése"}
           :uploaded
           {:en "Uploaded"
            :hu "Feltöltve"}
           :uploaded-at-n
           {:en "Uploaded at: %"
            :hu "Feltöltve: %"}
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
