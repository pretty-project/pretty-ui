
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.08.01
; Description:
; Version: v1.5.8
; Compatibility: x4.3.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A)
;  Az "_" -ra végződő azonosítójú kifejezések a billentyű-kódjukat is tartalmazzák.
;  Pl.: :app-menu_ => "App menu (m)"
;
; Books:
;  Appearance
;  Application
;  Database
;  Edit
;  Errors
;  Extensions
;  Law
;  Locales
;  Media
;  Notifications
;  Units
;  User



;; -- Appearance -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def appearance
     {:appearance
      {:en "Appearance"
       :hu "Kinézet"}
      :dark-theme
      {:en "Dark theme"
       :hu "Sötét téma"}
      :fullscreen-mode
      {:en "Fullscreen mode"
       :hu "Teljes képernyő"}
      :light-theme
      {:en "Light theme"
       :hu "Világos téma"}
      :list-view
      {:en "List items"
       :hu "Lista"}
      :select-color!
      {:en "Select color"
       :hu "Szín kiválasztása"}
      :select-theme!
      {:en "Select theme"
       :hu "Téma kiválasztása"}
      :selected-theme
      {:en "Selected theme"
       :hu "Kiválasztott téma"}
      :thumbnail-view
      {:en "Thumbnails"
       :hu "Bélyegképek"}
      :zoom-mode
      {:en "Zoom mode"
       :hu "Nagy nézet"}})



;; -- Application -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def application
     {:about-app
      {:en "About app"
       :hu "Az alkalmazás névjegye"}
      :accept!
      {:en "Accept"
       :hu "Rendben"}
      :app-menu
      {:en "App menu"
       :hu "Alkalmazás menü"}
      :app-menu_
      {:en "App menu (m)"
       :hu "Alkalmazás menü (m)"}
      :back!
      {:en "Back"
       :hu "Vissza"}
      :back-to-home!
      {:en "Back to home"
       :hu "Vissza a főoldalra"}
      :browser
      {:en "Browser"
       :hu "Böngésző"}
      :cancel!
      {:en "Cancel"
       :hu "Mégsem"}
      :close!
      {:en "Close"
       :hu "Bezárás"}
      :close!_
      {:en "Close (esc)"
       :hu "Bezárás (esc)"}
      :collapse!
      {:en "Hide"
       :hu "Elrejt"}
      :communication-error
      {:en "Commnication error"
       :hu "Kommunikációs hiba"}
      :control-panel
      {:en "Control panel"
       :hu "Vezérlőpult"}
      :copied-to-clipboard
      {:en "Copied to clipboard"
       :hu "Vágólapra helyezve"}
      :copy-link!
      {:en "Copy link"
       :hu "Hivatkozás másolása"}
      :create-a-backup!
      {:en "Create a backup"
       :hu "Biztonsági mentés készítése"}
      :create-new-project!
      {:en "New project"
       :hu "Új projekt"}
      :danger-zone
      {:en "Danger zone"
       :hu "Veszélyzóna"}
      :description
      {:en "Description"
       :hu "Leírás"}
      :documentation
      {:en "Documentation"
       :hu "Dokumentáció"}
      :do-not-show-again!
      {:en "Do not show again"
       :hu "Ne mutassa többet"}
      :done!
      {:en "Done"
       :hu "Kész"}
      :downloading-items...
      {:en "Downloading items ..."
       :hu "Elemek letöltése ..."}
      :exit!
      {:en "Exit"
       :hu "Kilépés"}
      :expand!
      {:en "Expand"
       :hu "Lenyit"}
      :filter!
      {:en "Filter"
       :hu "Szűrés"}
      :filter-conditions
      {:en "Filter conditions"
       :hu "Szűrési feltételek"}
      :filter-items!
      {:en "Filter items"
       :hu "Elemek szűrése"}
      :got-it!
      {:en "Got it!"
       :hu "Elfogadom"}
      :help
      {:en "Help"
       :hu "Súgó"}
      :help-center
      {:en "Help center"
       :hu "Súgó központ"}
      :hide!
      {:en "Hide"
       :hu "Elrejt"}
      :home-page_
      {:en "Home (h)"
       :hu "Kezdőlap (h)"}
      :home-page
      {:en "Home"
       :hu "Kezdőlap"}
      :just-a-moment
      {:en "Just a moment"
       :hu "Egy kis türelmet"}
      :label
      {:en "Label"
       :hu "Címke"}
      :less-options
      {:en "Less options"
       :hu "Kevesebb lehetőség"}
      :load!
      {:en "Load"
       :hu "Betöltés"}
      :loading
      {:en "Loading"
       :hu "Betöltés"}
      :maximize!
      {:en "Maximize"
       :hu "Teljes méret"}
      :minimize!
      {:en "Minimize"
       :hu "Kis méret"}
      :more-actions
      {:en "More actions"
       :hu "További műveletek"}
      :more-options
      {:en "More options"
       :hu "További lehetőségek"}
      :no-items-found
      {:en ""
       :hu "Nincs találat"}
      :need-help?
      {:en "Need help?"
       :hu "Súgó"}
      :n-items
      {:en "% items"
       :hu "% elem"}
      :no-options
      {:en "No options"
       :hu "Nincs választható lehetőség"}
      :no-option-selected
      {:en "No option selected"
       :hu "Nincs kiválasztott elem"}
      :no-options-selected
      {:en "No options selected"
       :hu "Nincsenek kiválasztott elemek"}
      :next!
      {:en "Next"
       :hu "Következő"}
      :next-step!
      {:en "Next"
       :hu "Tovább"}
      :no
      {:en "No"
       :hu "Nem"}
      :no-internet-connection
      {:en "No internet connection"
       :hu "Nincs internet kapcsolat"}
      :open!
      {:en "Open"
       :hu "Megnyitás"}
      :order-by
      {:en "Order by"
       :hu "Rendezés"}
      :packages
      {:en "Packages"
       :hu "Csomagok"}
      :preferences
      {:en "Preferences"
       :hu "Tulajdonságok"}
      :previous!
      {:en "Previous"
       :hu "Előző"}
      :properties
      {:en "Properties"
       :hu "Tulajdonságok"}
      :refresh!
      {:en "Refresh"
       :hu "Frissítés"}
      :retry!
      {:en "Retry"
       :hu "Újra"}
      :search
      {:en "Search"
       :hu "Keresés"}
      :settings
      {:en "Settings"
       :hu "Beállítások"}
      :shortcut-keys
      {:en "Shortcut keys"
       :hu "Billentyűparancsok"}
      :show-less!
      {:en "Show less"
       :hu "Mutass kevesebbet"}
      :show-more!
      {:en "Show more"
       :hu "Mutass többet"}
      :sort-by
      {:en "Sort by"
       :hu "Rendezés"}
      :status
      {:en "Status"
       :hu "Állapot"}
      :submit!
      {:en "Submit"
       :hu "Tovább"}
      :synchronization-error
      {:en "Synchronization error"
       :hu "Szinkronizációs probléma"}
      :synchronization-pending
      {:en "Synchronization pending"
       :hu "Szinkronizáció folyamatban"}
      :title
      {:en "Title"
       :hu "Cím"}
      :unnamed-item
      {:en "Unnamed item"
       :hu "Névtelen elem"}
      :unkown
      {:en "Unkown"
       :hu "Ismeretlen"}
      :upload!
      {:en "Upload"
       :hu "Feltöltés"}
      :uploading
      {:en "Uploading"
       :hu "Feltöltés folyamatban"}
      :version
      {:en "Version"
       :hu "Verziószám"}
      :view
      {:en "View"
       :hu "Nézet"}
      :yes
      {:en "Yes"
       :hu "Igen"}})



;; -- Database ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def database
     {:database
      {:en "Database"
       :hu "Adatbázis"}
      :data-editor
      {:en "Data editor"
       :hu "Adat szerkesztő"}
      :database-editor
      {:en "Database editor"
       :hu "Adatbázis szerkesztő"}
      :database-error
      {:en "Database error"
       :hu "Adatbázis hiba"}
      :database-manager
      {:en "Database manager"
       :hu "Adatbázis-kezelő"}
      :export-database!
      {:en "Export database"
       :hu "Adatbázis exportálása"}
      :import-database!
      {:en "Import database"
       :hu "Adatbázis importálása"}})



;; -- Developer ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def developer
     {:application-database-browser
      {:en "Configuration database browser"
       :hu "Konfigurációs adatbázis böngésző"}
      :developer-tools
      {:en "Developer tools"
       :hu "Fejlesztői eszközök"}
      :export-application-database!
      {:en "Export configuration database"
       :hu "Konfigurációs adatbázis exportálása"}
      :export-application-log!
      {:en "Export application log"
       :hu "Applikáció napló exportálása"}})



;; -- Edit --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def edit
     {:add-field!
      {:en "Add field"
       :hu "Mező hozzáadása"}
      :add-item!
      {:en "Add item"
       :hu "Elem hozzáadása"}
      :add-new!
      {:en "Add new"
       :hu "Hozzáadás"}
      :add-title!
      {:en "Add title"
       :hu "Címke hozzáadása"}
      :copy
      {:en "Copy"
       :hu "Másolat"}
      :copy!
      {:en "Copy"
       :hu "Másolás"}
      :create!
      {:en "Create"
       :hu "Létrehozás"}
      :create-item!
      {:en "Create item"
       :hu "Elem létrehozása"}
      :delete!
      {:en "Delete"
       :hu "Törlés"}
      :delete-field!
      {:en "Delete field"
       :hu "Mező törlése"}
      :delete-selected-items!
      {:en "Delete selected items"
       :hu "Kiválasztott elemek törlése"}
      :delete-selected-items?
      {:en "Are you sure you want to delete selected items?"
       :hu "Biztos vagy benne, hogy szeretnéd törölni a kiválasztott elemeket?"}
      :deselect!
      {:en "Deselect"
       :hu "Kiválasztás megszüntetése"}
      :duplicate-item!
      {:en "Duplicate item"
       :hu "Elem duplikálása"}
      :duplicate!
      {:en "Duplicate"
       :hu "Duplikálás"}
      :edit!
      {:en "Edit"
       :hu "Szerkesztés"}
      :edit-content!
      {:en "Edit content"
       :hu "Tartalom szerkesztése"}
      :edit-copy!
      {:en "Edit copy"
       :hu "Másolat szerkesztése"}
      :edit-description!
      {:en "Edit description"
       :hu "Leírás szerkesztése"}
      :edit-label!
      {:en "Edit label"
       :hu "Címke szerkesztése"}
      :edit-title!
      {:en "Edit title"
       :hu "Címke szerkesztése"}
      :editor
      {:en "Editor"
       :hu "Szerkesztő"}
      :empty-field!
      {:en "Empty field"
       :hu "Mező ürítése"}
      :item-added
      {:en "Item added"
       :hu "Elem hozzáadva"}
      :item-duplicated
      {:en "Item duplicated"
       :hu "Elem másolva"}
      :item-does-not-exists
      {:en "This item does not exists"
       :hu "Ez az elem még nem létezik"}
      :item-removed
      {:en "Item removed"
       :hu "Elem törölve"}
      :move!
      {:en "Move"
       :hu "Áthelyezés"}
      :new
      {:en "New"
       :hu "Új"}
      :no-changes-to-redo
      {:en "No changes to redo"
       :hu "Nincs megismételhető változás"}
      :no-changes-to-save
      {:en "No changes to save"
       :hu "Nincs elmenthető változás"}
      :no-changes-to-undo
      {:en "No changes to undo"
       :hu "Nincs visszavonható változás"}
      :paste!
      {:en "Paste"
       :hu "Beillesztés"}
      :please-check-this-field
      {:en "Please check this field!"
       :hu "Kérlek jelöld ki ezt a mezőt!"}
      :please-fill-out-this-field
      {:en "Please fill out this field!"
       :hu "Kérlek töltsd ki ezt a mezőt!"}
      :please-select-an-option
      {:en "Please select an option!"
       :hu "Kérlek válassz a lehetőségek közül!"}
      :redo!
      {:en "Redo"
       :hu "Megismétlés"}
      :rename!
      {:en "Rename"
       :hu "Átnevezés"}
      :reset!
      {:en "Reset"
       :hu "Visszaállítás"}
      :remove!
      {:en "Remove"
       :hu "Eltávolítás"}
      :remove-item?
      {:en "Are you sure you want to remove this item?"
       :hu "Biztos vagy benne, hogy szeretnéd eltávolítani ezt az elemet?"}
      :remove-item!
      {:en "Remove item"
       :hu "Elem eltávolítása"}
      :reset-field!
      {:en "Reset field"
       :hu "Mező visszaállítása"}
      :save!
      {:en "Save"
       :hu "Mentés"}
      :save-changes!
      {:en "Save changes"
       :hu "Változások mentése"}
      :save-changes?
      {:en "Are you sure you want to save changes?"
       :hu "Biztos vagy benne, hogy szeretnéd elmenteni a változásokat?"}
      :save-n-changes
      {:en " changes to be saved"
       :hu " változtatás mentése"}
      :saved
      {:en "Saved"
       :hu "Sikeres mentés"}
      :saving-error
      {:en "Saving error"
       :hu "Sikertelen mentés"}
      :select-all-items!
      {:en "Select all items"
       :hu "Összes elem kiválasztása"}
      :select!
      {:en "Select"
       :hu "Válassz"}
      :select
      {:en "Select"
       :hu "Kiválasztás"}
      :check
      {:en "Select"
       :hu "Kijelölés"}
      :uncheck-selected!
      {:en "Uncheck selected"
       :hu "Kiválasztás megszűntetése"}
      :undo!
      {:en "Undo"
       :hu "Visszavonás"}
      :undo-delete!
      {:en "Undo delete"
       :hu "Törlés visszavonása"}
      :undo-all-changes?
      {:en "Are you sure you want to undo all changes?"
       :hu "Biztos vagy benne, hogy szeretnéd visszavonni a változásokat?"}
      :undo-changes!
      {:en "Undo changes"
       :hu "Változások visszavonása"}
      :unselect
      {:en "Unselect"
       :hu "Kiválasztás megszűntetése"}
      :write-something!
      {:en "Write something!"
       :hu "Írj valamit!"}})



;; -- Errors ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def errors
     {:error-report
      {:en "Error report"
       :hu "Hibajelentés"}
      :error-report-sent
      {:en "Error report sent"
       :hu "Hibajelentés elküldve"}
      :enable-sending-error-reports!
      {:en "Enable sending error reports"
       :hu "Hibajelentések küldésének engedélyezése"}
      :no-internet-connection
      {:en "No internet connection"
       :hu "Nincs internet kapcsolat"}
      :page-is-not-available
      {:en "Sorry, this page is not available"
       :hu "A keresett oldal nem található"}
      ;:hu "A megadott címen ..."
      :page-not-found
      {:en "Page not found"
       :hu "Az oldal nem található"}
      :please-check-your-internet-connection
      {:en "Please check your internet connection and try to reload"
       :hu ""}
      ;:hu "Kérlek ellenőrízd a ..."
      :send-error-report!
      {:en "Send error report"
       :hu "Hibajelentés küldése"}
      :sending-error-reports
      {:en "Send error reports"
       :hu "Hibajelentések küldése"}
      :service-not-available
      {:en "Service not available"
       :hu "A szolgáltatás nem elérhető"}
      :the-link-you-followed-may-be-broken
      {:en "The link you followed may be broken, or the page may have been removed"
       :hu ""}
      :you-do-not-have-internet-connection
      {:en "You do not have internet connection"
       :hu "Nincs internet kapcsolatod"}})



;; -- Extensions --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def extensions
     {:actions
      {:en "Actions"
       :hu "Műveletek"}
      :add-client
      {:en "Add client"
       :hu "Ügyfél hozzáadása"}
      :calendar
      {:en "Calendar"
       :hu "Naptár"}
      :charts
      {:en "Charts"
       :hu "Charts"}
      :clients
      {:en "Clients"
       :hu "Ügyfelek"}
      :client-id
      {:en "Client ID"
       :hu "Ügyfél azonosító"}
      :devices
      {:en "Devices"
       :hu "Eszközök"}
      :employees
      {:en "Employees"
       :hu "Alkamazottak"}
      :edit-client
      {:en "Edit client"
       :hu "Ügyfél szerkesztése"}
      :extensions
      {:en "Extensions"
       :hu "Eszközök"}
      :inventories
      {:en "Inventories"
       :hu "Raktárkészlet"}
      :jobs
      {:en "Jobs"
       :hu "Munkák"}
      :machines
      {:en "Machines"
       :hu "Gépek"}
      :price-quote
      {:en "Price quote"
       :hu "Árajánlat"}
      :services
      {:en "Services"
       :hu "Szolgáltatások"}
      :show-extensions!
      {:en "Show extensions"
       :hu "Kiegészítők"}
      :show-extensions!_
      {:en "Show extensions (e)"
       :hu "Kiegészítők (e)"}
      :unit-price
      {:en "Unit price"
       :hu "Egységár"}
      :unit-quantity
      {:en "Unit quantity"
       :hu "Mennyiségi egység"}
      :vehicles
      {:en "Vehicles"
       :hu "Járművek"}
      :webshop
      {:en "Webshop"
       :hu "Webshop"}
      :webshops
      {:en "Webshops"
       :hu "Webshopok"}
      :website-menu
      {:en "Website menu"
       :hu "Weboldal menu"}
      :websites
      {:en "Websites"
       :hu "Weboldalak"}})



;; -- Law ---------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def law
     {:analytics-cookies
       {:en "Analytics cookies"
        :hu "Statisztikai sütik"}
       :cookie-settings
       {:en "Cookie settings"
        :hu "Süti beállítások"}
       :necessary-cookies
       {:en "Necessary cookies"
        :hu "Szükséges sütik"}
       :privacy
       {:en "Privacy"
        :hu "Adatvédelem"}
       :privacy-policy
       {:en "Privacy policy"
        :hu "Adatkezelési tájékoztató"}
       :privacy-settings
       {:en "Privacy settings"
        :hu "Adatvédelmi beállítások"}
       :remove-stored-cookies!
       {:en "Remove stored cookies"
        :hu "Tárolt sütik eltávolítása"}
       :remove-stored-cookies?
       {:en "Are you sure you want to remove stored cookies?"
        :hu "Biztos vagy benne, hogy szeretnéd eltávolítani a tárolt sütiket?"}
       :this-website-uses-cookies
       {:en "This website uses cookies to analyze traffic and ensure you get the best experience"
        :hu "Ez a webhely sütiket használ statisztikai adatok gyűjtésére és a jobb felhasználói élmény biztosítására"}
       :terms-of-service
       {:en "Terms of service"
        :hu "Szerződési feltételek"}
       :use-cookies
       {:en "Use cookies"
        :hu "Sütik használata"}
       :user-experience-cookies
       {:en "User-experience cookies"
        :hu "Felhasználói élmény sütik"}
       :what-cookies-are?
       {:en "What cookies are?"
        :hu "Mik azok a sütik?"}})



;; -- Locales -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def locales
     {:language
      {:en "Language"
       :hu "Nyelv"}
      :languages
      {:en "Languages"
       :hu "Nyelvek"}
      :save-in-all-languages
      {:en "Save in all languages"
       :hu "Mentés minden nyelven"}
      :en
      {:en "English"
       :hu "English"}
      :en-gb
      {:en "English - GB"
       :hu "English - GB"}
      :en-us
      {:en "English - US"
       :hu "English - US"}
      :fr-fr
      {:en "Français"
       :hu "Français"}
      :de-de
      {:en "Deutsch"
       :hu "Deutsch"}
      :hu
      {:en "Magyar"
       :hu "Magyar"}
      :it-it
      {:en "Italiano"
       :hu "Italiano"}
      :ro
      {:en "Română"
       :hu "Română"}})



;; -- Media -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def media
     {:add-or-remove-files!
      {:en "Add or remove files"
       :hu "Fájlok hozzáadása vagy eltávolítása"}
      :attach-file!
      {:en "Attach a file"
       :hu "Fájl hozzáadása"}
      :attach-selected-files!
      {:en "Attach selected files"
       :hu "Kiválasztott fájlok csatolása"}
      :by-created-at
      {:en "Created at"
       :hu "Létrehozás dátuma szerint"}
      :by-date
      {:en "By date"
       :hu "Dátum szerint"}
      :by-name
      {:en "Name"
       :hu "Név szerint"}
      :by-order
      {:en "By order"
       :hu "Sorrend szerint"}
      :by-size
      {:en "By size"
       :hu "Méret szerint"}
      :by-uploaded-at
      {:en "Uploaded at"
       :hu "Feltöltés ideje szerint"}
      :content-size
      {:en "Content size"
       :hu "Tartalom"}
      :create-directory!
      {:en "Create directory"
       :hu "Mappa létrehozása"}
      :create-new-directory!
      {:en "Create new directory"
       :hu "Új mappa létrehozása"}
      :delete-directory!
      {:en "Delete directory"
       :hu "Mappa törlése"}
      :delete-file!
      {:en "Delete file"
       :hu "Fájl törlése"}
      :delete-directory?
      {:en "Are you sure you want to delete this directory?"
       :hu "Biztos vagy benne, hogy szeretnéd törölni ezt a mappát?"}
      :delete-file?
      {:en "Are you sure you want to delete this file?"
       :hu "Biztos vagy benne, hogy szeretnéd törölni ezt a fájlt?"}
      :deleting-files-and-directories-is-not-reversible
      {:en "Deleting files and directories is not reversible!"
       :hu "A fájlok és mappák törlése nem visszavonható!"}
      :directory-created
      {:en "Directory created"
       :hu "Mappa létrehozva"}
      :directory-does-not-exists
      {:en "This is not the directory you are looking for."
       :hu "A mappa nem található!"}
      :directory-name
      {:en "Directory name"
       :hu "Mappa neve"}
      :download!
      {:en "Download"
       :hu "Letöltés"}
      :download-image!
      {:en "Download image"
       :hu "Kép letöltése"}
      :drop-files-here-to-upload
      {:en "Drop files here to upload!"
       :hu "Húzd ide a fájlokat, amiket fel szeretnél tölteni!"}
      :empty-directory
      {:en "Empty directory"
       :hu "Üres mappa"}
      :filename
      {:en "Filename"
       :hu "Fájlnév"}
      :filesize
      {:en "Filesize"
       :hu "Fájlméret"}
      :files-uploaded
      {:en "Files uploaded"
       :hu "Sikeres fájlfeltöltés"}
      :file-upload-failure
      {:en "File upload failure"
       :hu "Sikertelen fájlfeltöltés"}
      :file-uploading-in-progress
      {:en "File uploading in progress"
       :hu "Fájlfeltöltés folyamatban"}
      :file-manager
      {:en "File manager"
       :hu "Fájlkezelő"}
      :file-not-found
      {:en "File not found"
       :hu "A fájl nem található"}
      :file-uploader
      {:en "File uploader"
       :hu "Fájlfeltöltő"}
      :free-some-space
      {:en "Free % space!"
       :hu "Szabadítson fel %1 %2 szabad helyet!"}
      :image-gallery
      {:en "Image gallery"
       :hu "Képgaléria"}
      :invalid-directory-name
      {:en "Invalid name!"
       :hu "Nem megfelelő név!"}
      :invalid-filename
      {:en "Invalid filename!"
       :hu "Nem megfelelő fájlnév!"}
      :last-modified
      {:en "Last modified"
       :hu "Utoljára módosítva"}
      :new-directory
      {:en "New directory"
       :hu "Új mappa"}
      :no-files-selected
      {:en "No files selected."
       :hu "Nincsenek kiválaszott fájlok."}
      :remove-file!
      {:en "Remove file"
       :hu "Fájl eltávolítása"}
      :rename-directory!
      {:en "Rename directory"
       :hu "Mappa átnevezése"}
      :rename-file!
      {:en "Rename file"
       :hu "Fájl átnevezése"}
      :save-file?
      {:en "Are you sure you want to save this file to device?"
       :hu "Biztos vagy benne, hogy szeretnéd menteni ezt a fájlt az eszközre?"}
      :select-the-files-you-would-like-to-attach
      {:en "Select the files you would like to attach!"
       :hu "Válaszd ki a csatolni kívánt fájlokat!"}
      :share-image
      {:en "Share image"
       :hu "Kép megosztása"}
      :there-is-not-enough-space
      {:en "There is not enough space available to complete this operation."
       :hu "Nincs elegendő hely a művelet befejezéséhez."}
      :untitled-directory
      {:en "Untitled directory"
       :hu "Névtelen mappa"}
      :upload-files!
      {:en "Upload files"
       :hu "Fájlok feltöltése"}
      :upload-files-from-device!
      {:en "Upload files from device"
       :hu "Fájlok feltöltése az eszközről"}
      :uploaded-at
      {:en "Uploaded at: "
       :hu "Feltöltve: "}
      :uploaded-files
      {:en "Uploaded files"
       :hu "Feltöltött fájlok"}
      :uploaded-images
      {:en "Uploaded images"
       :hu "Feltöltött képek"}
      :uploading-file-count
      {:en "Uploading file count"
       :hu "Feltöltésre váró fájlok száma"}
      :uploading-files-size
      {:en "Uploading files size"
       :hu "Feltöltésre váró fájlok mérete"}})



;; -- Notifications -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(def notifications
    {:notifications
     {:en "Notifications"
      :hu "Értesítések"}
     :notification-bubbles
     {:en "Notification bubbles"
      :hu "Értesítési buborékok"}
     :notification-messages
     {:en "Notification messages"
      :hu "Értesítési üzenetek"}
     :notification-sounds
     {:en "Notification sounds"
      :hu "Hangjelzések"}})



;; -- Units -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def units
     {:day
      {:en "Day"
       :hu "Nap"}
      :hour
      {:en "Hour"
       :hu "Óra"}
      :minute
      {:en "Minute"
       :hu "Perc"}
      :month
      {:en "Month"
       :hu "Hónap"}
      :piece
      {:en "Piece"
       :hu "Darab"}
      :second
      {:en "Second"
       :hu "Másodperc"}
      :year
      {:en "Year"
       :hu "Év"}
      :weight
      {:en "Weight"
       :hu "Tömeg"}})



;; -- User --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def user
     {:change-password!
      {:en "Change password"
       :hu "Jelszó megváltoztatása"}
      :change-pin!
      {:en "Change PIN"
       :hu "PIN megváltoztatása"}
      :change-profile-picture
      {:en "Change profile picture"
       :hu "Profilkép megváltoztatása"}
      :change-username
      {:en "Change username"
       :hu "Felhasználónév módosítása"}
      :clear-user-data!
      {:en "Clear user data"
       :hu "Felhasználó adatainak törlése"}
      :continue-as
      {:en "Continue as "
       :hu "Folytatás, mint "}
      :create-account!
      {:en "Create account"
       :hu "Regisztráció"}
      :delete-account!
      {:en "Delete account"
       :hu "Fiók törlése"}
      :delete-user-account!
      {:en "Delete user account"
       :hu "Felhasználói fiók törlése"}
      :email-address
      {:en "Email address"
       :hu "Email cím"}
      :email
      {:en "Email"
       :hu "Email"}
      :first-name
      {:en "First name"
       :hu "Keresztnév"}
      :last-name
      {:en "Last name"
       :hu "Vezetéknév"}
      :forgot-password
      {:en "Forgot password"
       :hu "Elfelejtettem a jelszavam"}
      :hide-password!
      {:en "Hide password"
       :hu "Jelszó elrejtése"}
      :incorrect-email-address-or-password
      {:en "Incorrect email address or password"
       :hu "Hibás email cím vagy jelszó"}
      :invalid-email-address
      {:en "Invalid email address"
       :hu "Érvénytelen email cím"}
      :invalid-phone-number
      {:en "Invalid phone number"
       :hu "Érvénytelen telefonszám"}
      :login!
      {:en "Login"
       :hu "Bejelentkezés"}
      :logout!
      {:en "Logout"
       :hu "Kijelentkezés"}
      :logout-failed
      {:en "Logout failed!"
       :hu "Sikertelen kijelentkezés!"}
      :my-profile
      {:en "My profile"
       :hu "Profilom"}
      :name
      {:en "Name"
       :hu "Név"}
      :new-password
      {:en "New password"
       :hu "Új jelszó"}
      :new-password-again
      {:en "New password again"
       :hu "Új jelszó mégegyszer"}
      :new-pin
      {:en "New PIN"
       :hu "Új PIN"}
      :new-pin-again
      {:en "New PIN again"
       :hu "Új PIN mégegyszer"}
      :old-password
      {:en "Old password"
       :hu "Régi jelszó"}
      :old-pin
      {:en "Old PIN"
       :hu "Régi PIN"}
      :password
      {:en "Password"
       :hu "Jelszó"}
      :password-is-too-weak
      {:en "Password is too weak"
       :hu "Túl gyenge jelszó"}
      :permission-denied
      {:en "Permission denied!"
       :hu "Hozzáférés megtagadva!"}
      :phone
      {:en "Phone"
       :hu "Telefon"}
      :phone-number
      {:en "Phone number"
       :hu "Telefonszám"}
      :pin
      {:en "PIN"
       :hu "PIN"}
      :show-password!
      {:en "Show password"
       :hu "Jelszó mutatása"}
      :signed-in-as
      {:en "Signed in as "
       :hu "Bejelentkezve, mint "}
      :username
      {:en "Username"
       :hu "Felhasználónév"}
      :valid-password-rules
      {:en "Password must contain at least 6 characters, both uppercase and lowercase letters, and a number!"
       :hu "A jelszó legyen legalább 6 karakter hosszú, tartalmazzon kis- és nagybetűket és legalább egy számot!"}})



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOKS (merge appearance
                  application
                  database
                  developer
                  edit
                  errors
                  extensions
                  law
                  locales
                  media
                  notifications
                  units
                  user))
