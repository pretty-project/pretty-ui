

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.transfer)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:download!
           {:en "Download"
            :hu "Letöltés"}
           :download-selected-items!
           {:en "Download selected items"
            :hu "Kijelölt elemek letöltése"}
           :downloading...
           {:en "Downloading ..."
            :hu "Letöltés ..."}
           :downloading-items...
           {:en "Downloading items ..."
            :hu "Elemek letöltése ..."}
           :downloaded-at
           {:en "Downloaded at: "
            :hu "Letöltve: "}
           :downloaded-items
           {:en "Downloaded items"
            :hu "Letöltött elemek"}
           :downloads
           {:en "Downloads"
            :hu "Letöltések"}
           :n-items-downloaded
           {:en "% item(s) downloaded"
            :hu "% elem letöltve"}
           :n-items-uploaded
           {:en "% item(s) uploaded"
            :hu "% elem feltöltve"}
           :npn-items-downloaded
           {:en "%1 / %2 item(s) downloaded"
            :hu "%1 / %2 elem letöltve"}
           :npn-items-uploaded
           {:en "%1 / %2 item(s) uploaded"
            :hu "%1 / %2 elem feltöltve"}
           :preparing-to-download...
           {:en "Preparing to download ..."
            :hu "Letöltés előkészítése ..."}
           :preparing-to-upload...
           {:en "Preparing to upload ..."
            :hu "Feltöltés előkészítése ..."}
           :upload!
           {:en "Upload"
            :hu "Feltöltés"}
           :upload-selected-items!
           {:en "Upload selected items"
            :hu "Kijelölt elemek feltöltése"}
           :uploaded-at
           {:en "Uploaded at: "
            :hu "Feltöltve: "}
           :uploaded-items
           {:en "Uploaded items"
            :hu "Feltöltött elemek"}
           :uploading...
           {:en "Uploading ..."
            :hu "Feltöltés ..."}
           :uploads
           {:en "Uploads"
            :hu "Feltöltések"}})
