
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.08.01
; Description:
; Version: v1.8.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books
    (:require [x.mid-dictionary.extension-books :refer [EXTENSION-BOOKS]]

              ; TEMP
              [x.mid-dictionary.books.actions       :as books.actions]
              [x.mid-dictionary.books.appearance    :as books.appearance]
              [x.mid-dictionary.books.locales       :as books.locales]
              [x.mid-dictionary.books.media         :as books.media]
              [x.mid-dictionary.books.notifications :as books.notifications]
              [x.mid-dictionary.books.social-media  :as books.social-media]
              [x.mid-dictionary.books.sync          :as books.sync]
              [x.mid-dictionary.books.units         :as books.units]
              [x.mid-dictionary.books.user          :as books.user]
              [x.mid-dictionary.books.view          :as books.view]))



;; -- Application -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def application
     {:invalid-name
      {:en "Invalid name!"
       :hu "Nem megfelelő név!"}

      :last-modified
      {:en "Last modified"
       :hu "Utoljára módosítva"}
      :last-modified-at-n
      {:en "Last modified at: %"
       :hu "Utoljára módosítva: %"}
      :size-n
      {:en "Size: %"
       :hu "Méret: %"}
      :failed-to-update
      {:en "Failed to update"
       :hu "Sikertelen változtatás"}




      :about-app
      {:en "About app"
       :hu "Az alkalmazás névjegye"}

      :active
      {:en "Active"
       :hu "Aktív"}

      :app-menu
      {:en "App menu"
       :hu "Alkalmazás menü"}

      :back-to-home!
      {:en "Back to home"
       :hu "Vissza a főoldalra"}
      :basic-info
      {:en "Basic info"
       :hu "Alapvető adatok"}
      :browser
      {:en "Browser"
       :hu "Böngésző"}

      :category
      {:en "Category"
       :hu "Kategória"}
      :categories
      {:en "Categories"
       :hu "Kategóriák"}



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

      :extensions
      {:en "Extensions"
       :hu "Eszközök"}
      :favorites
      {:en "Favorites"
       :hu "Kedvencek"}
      :filter!
      {:en "Filter"
       :hu "Szűrés"}
      :filters
      {:en "Filters"
       :hu "Szűrők"}
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

      :loading
      {:en "Loading"
       :hu "Betöltés"}
      :maximize!
      {:en "Maximize"
       :hu "Teljes méret"}
      :minimize!
      {:en "Minimize"
       :hu "Kis méret"}
      :menu
      {:en "Menu"
       :hu "Menü"}
      :more-actions
      {:en "More actions"
       :hu "További műveletek"}
      :more-info
      {:en "More info"
       :hu "További adatok"}
      :more-options
      {:en "More options"
       :hu "További lehetőségek"}
      :no-item-selected
      {:en "No item selected"
       :hu "Nincs kiválasztott elem"}
      :no-items-selected
      {:en "No items selected"
       :hu "Nincsenek kiválasztott elemek"}
      :no-items-found
      {:en ""
       :hu "Nincs találat"}
      :no-items-to-show
      {:en "No items to show"
       :hu "Nincsenek megjeleníthető elemek"}
      :need-help?
      {:en "Need help?"
       :hu "Súgó"}

      :n-items
      {:en "% items"
       :hu "% elem"}

      :n-items-selected
      {:en "% item(s) selected"
       :hu "% elem kiválasztva"}
      :no-options
      {:en "No options"
       :hu "Nincs választható elem"}
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



      :packages
      {:en "Packages"
       :hu "Csomagok"}
      :playground
      {:en "Playground"
       :hu "Playground"}
      :preferences
      {:en "Preferences"
       :hu "Tulajdonságok"}

      :preview
      {:en "Preview"
       :hu "Előnézet"}
      :previous!
      {:en "Previous"
       :hu "Előző"}
      :properties
      {:en "Properties"
       :hu "Tulajdonságok"}
      
      :removed-from-favorites
      {:en "Removed from favorites"
       :hu "Eltávolítva a kedvencek közül"}
      :retry!
      {:en "Retry"
       :hu "Újra"}
      :sample
      {:en "Sample"
       :hu "Minta"}
      :search
      {:en "Search"
       :hu "Keresés"}
      :seo
      {:en "Search Engine Optimization"
       :hu "Keresőoptimalizálás"}
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
      :status
      {:en "Status"
       :hu "Állapot"}
      :subcategory
      {:en "Subcategory"
       :hu "Alkategória"}
      :subcategories
      {:en "Subcategories"
       :hu "Alkategóriák"}
      :submit!
      {:en "Submit"
       :hu "Tovább"}

      :title
      {:en "Title"
       :hu "Cím"}
      :unnamed-item
      {:en "Unnamed item"
       :hu "Névtelen elem"}
      :unkown
      {:en "Unkown"
       :hu "Ismeretlen"}

      :version
      {:en "Version"
       :hu "Verziószám"}
      :view
      {:en "View"
       :hu "Nézet"}
      :welcome-n
      {:en "Welcome %!"
       :hu "Üdv %!"}
      :yes
      {:en "Yes"
       :hu "Igen"}})



;; -- Contacts ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def contacts
     {:contacts
      {:en "Contacts"
       :hu "Kapcsolat"}
      :contacts-data
      {:en "Contacts data"
       :hu "Kapcsolati adatok"}
      :email-address
      {:en "Email address"
       :hu "Email cím"}
      :email
      {:en "Email"
       :hu "Email"}
      :first-name
      {:en "First name"
       :hu "Keresztnév"}
      :google-maps-link
      {:en "Google Maps link"
       :hu "Google Térkép hivatkozás"}
      :last-name
      {:en "Last name"
       :hu "Vezetéknév"}
      :name
      {:en "Name"
       :hu "Név"}
      :phone
      {:en "Phone"
       :hu "Telefon"}
      :phone-number
      {:en "Phone number"
       :hu "Telefonszám"}
      :vat-no
      {:en "VAT number"
       :hu "Adószám"}})



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
     {:add!
      {:en "Add"
       :hu "Hozzáadás"}
      :add-field!
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
      :all-items
      {:en "All items"
       :hu "Összes elem"}
      :archive-selected-items?
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
      :changes-discarded
      {:en "Changes discarded"
       :hu "Változások elvetve"}
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
      :delete-item!
      {:en "Delete item"
       :hu "Elem törlése"}
      :delete-selected-items!
      {:en "Delete selected items"
       :hu "Kiválasztott elemek törlése"}
      :delete-selected-items?
      {:en "Are you sure you want to delete selected items?"
       :hu "Biztos vagy benne, hogy szeretnéd törölni a kiválasztott elemeket?"}
      :deselect!
      {:en "Deselect"
       :hu "Kiválasztás megszüntetése"}
      :draft
      {:en "Draft"
       :hu "Vázlat"}
      :duplicate-item!
      {:en "Duplicate item"
       :hu "Elem duplikálása"}
      :duplicate!
      {:en "Duplicate"
       :hu "Duplikálás"}
      :duplicate-selected-items?
      {:en "Are you sure you want to duplicate selected items?"
       :hu "Biztos vagy benne, hogy szeretnéd másolni a kiválasztott elemeket?"}
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
      :item-archived
      {:en "Item archived"
       :hu "Elem archiválva"}
      :item-unarchived
      {:en "Item unarchived"
       :hu "Elem dearchiválva"}
      :item-deleted
      {:en "Item deleted"
       :hu "Elem törölve"}
      :item-duplicated
      {:en "Item duplicated"
       :hu "Elem duplikálva"}
      :item-does-not-exists
      {:en "This item does not exists"
       :hu "Ez az elem még nem létezik"}
      :item-removed
      {:en "Item removed"
       :hu "Elem törölve"}
      :make-a-copy!
      {:en "Make a copy"
       :hu "Másolat készítése"}
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
      :n-items-archived
      {:en "% item(s) archived"
       :hu "% elem archiválva"}
      :n-items-unarchived
      {:en "% item(s) unarchived"
       :hu "% elem dearchiválva"}
      :n-items-deleted
      {:en "% item(s) deleted"
       :hu "% elem törölve"}
      :n-items-duplicated
      {:en "% item(s) duplicated"
       :hu "% elem duplikálva"}
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
       :hu "Kérlek válassz az elemek közül!"}
      :recover!
      {:en "Recover"
       :hu "Visszaállítás"}
      :redo!
      {:en "Redo"
       :hu "Megismétlés"}
      :rename!
      {:en "Rename"
       :hu "Átnevezés"}
      :reorder
      {:en "Reorder"
       :hu "Egyedi sorrend"}
      :remove!
      {:en "Remove"
       :hu "Eltávolítás"}
      :remove-item?
      {:en "Are you sure you want to remove this item?"
       :hu "Biztos vagy benne, hogy szeretnéd eltávolítani ezt az elemet?"}
      :remove-item!
      {:en "Remove item"
       :hu "Elem eltávolítása"}
      :reset!
      {:en "Reset"
       :hu "Visszaállítás"}
      :reset-field!
      {:en "Reset field"
       :hu "Mező visszaállítása"}
      :restore!
      {:en "Restore"
       :hu "Visszaállítás"}
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
      :save-order!
      {:en "Save order"
       :hu "Sorrend mentése"}
      :saved
      {:en "Saved"
       :hu "Sikeres mentés"}
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
      :unarchive!
      {:en "Unarchive"
       :hu "Dearchiválás"}
      :unarchived
      {:en "Unarchived"
       :hu "Dearchiválva"}
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
      :unsaved-changes-discarded
      {:en "Unsaved changes discarded"
       :hu "Nem mentett változások elvetve"}
      :unselect!
      {:en "Unselect"
       :hu "Kijelölés megszűntetése"}
      :write-something!
      {:en "Write something!"
       :hu "Írj valamit!"}})



;; -- Errors ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def errors
     {:an-error-occured
      {:en "An error occured"
       :hu "Hiba történt"}
      :error-occured
      {:en "Error occured"
       :hu "Hiba történt"}
      :error-report
      {:en "Error report"
       :hu "Hibajelentés"}
      :error-report-sent
      {:en "Error report sent"
       :hu "Hibajelentés elküldve"}
      :enable-sending-error-reports!
      {:en "Enable sending error reports"
       :hu "Hibajelentések küldésének engedélyezése"}
      :failed-to-copy
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
      :no-internet-connection
      {:en "No internet connection"
       :hu "Nincs internet kapcsolat"}
      :page-is-not-available
      {:en "Sorry, this page is not available"
       :hu "A keresett oldal nem található"}
      :page-is-under-construction
      {:en "Sorry, this page is under construction"
       :hu "A keresett oldal fejlesztés alatt áll"}
      :page-is-under-maintenance
      {:en "Sorry, this page is under maintenance"
       :hu "A keresett oldal pillanatnyilag nem elérhető"}
      :page-not-found
      {:en "Page not found"
       :hu "Az oldal nem található"}
      :please-check-your-internet-connection
      {:en "Please check your internet connection!"
       :hu "Kérlek ellenőrizd az internet kapcsolatot!"}
      :please-check-back-soon...
      {:en "Please check back soon ..."
       :hu "Kérlek nézz vissza később ..."}
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
      ; Always place a comma before or when it begins an independent clause!
      {:en "The link you followed may be broken, or the page may have been removed"
       :hu "Előfordulhat, hogy a megadott hivatkozás nem megfelelő, vagy az oldalt áthelyeztük"}
      :the-item-you-opened-may-be-broken
      {:en "The item you want to open may be broken or removed"
       :hu "Előfordulhat, hogy az elem sérült vagy már nem elérhető"}
      :you-do-not-have-internet-connection
      {:en "You do not have internet connection"
       :hu "Nincs internet kapcsolatod"}
      :you-do-not-have-permission-to-view-this-page
      {:en "You do not have permission to view this page!"
       :hu "Nincs jogosultságod az oldal megtekintéséhez!"}})



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
        :hu "Felhasználói élmény sütik"}})



;; -- Locale ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def locale
     {:address
      {:en "Address"
       :hu "Cím"}
      :city
      {:en "City"
       :hu "Város"}
      :country
      {:en "Country"
       :hu "Ország"}
      :zip-code
      {:en "ZIP code"
       :hu "Irányítószám"}})




;; -- Order --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def order-by
    {:by-created-at
     {:en "By created at"
      :hu "Létrehozás ideje szerint"}
     :by-created-at-ascending
     {:en "By created at (ascending)"
      :hu "Létrehozás ideje szerint (növekvő)"}
     :by-created-at-descending
     {:en "By created at (descending)"
      :hu "Létrehozás ideje szerint (csökkenő)"}
     :by-date
     {:en "By date"
      :hu "Dátum szerint"}
     :by-date-ascending
     {:en "By date (ascending)"
      :hu "Dátum szerint (növekvő)"}
     :by-date-descending
     {:en "By date (descending)"
      :hu "Dátum szerint (csökkenő)"}
     :by-modified-at
     {:en "By date"
      :hu "Dátum szerint"}
     :by-modified-at-ascending
     {:en "By date (ascending)"
      :hu "Dátum szerint (növekvő)"}
     :by-modified-at-descending
     {:en "By date (descending)"
      :hu "Dátum szerint (csökkenő)"}
     :by-name
     {:en "By name"
      :hu "Név szerint"}
     :by-name-ascending
     {:en "By name (ascending)"
      :hu "Név szerint (növekvő)"}
     :by-name-descending
     {:en "By name (descending)"
      :hu "Név szerint (csökkenő)"}
     :by-order
     {:en "By order"
      :hu "Sorrend szerint"}
     :by-order-ascending
     {:en "By order (ascending)"
      :hu "Sorrend szerint (növekvő)"}
     :by-order-descending
     {:en "By order (descending)"
      :hu "Sorrend szerint (csökkenő)"}
     :by-size
     {:en "By size"
      :hu "Méret szerint"}
     :by-size-ascending
     {:en "By size (ascending)"
      :hu "Méret szerint (növekvő)"}
     :by-size-descending
     {:en "By size (descending)"
      :hu "Méret szerint (csökkenő)"}
     :by-uploaded-at
     {:en "By uploaded at"
      :hu "Feltöltés ideje szerint"}
     :by-uploaded-at-ascending
     {:en "By uploaded at (ascending)"
      :hu "Feltöltés ideje szerint (növekvő)"}
     :by-uploaded-at-descending
     {:en "By uploaded at (descending)"
      :hu "Feltöltés ideje szerint (csökkenő)"}
     :order-by
     {:en "Order by"
      :hu "Rendezés"}
     :sort-by
     {:en "Sort by"
      :hu "Rendezés"}})





;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOKS (merge books.actions/BOOK
                  books.appearance/BOOK
                  application
                  contacts
                  database
                  developer
                  edit
                  errors
                  law
                  locale
                  books.locales/BOOK
                  books.media/BOOK
                  books.notifications/BOOK
                  order-by
                  books.social-media/BOOK
                  books.sync/BOOK
                  books.units/BOOK
                  books.user/BOOK
                  EXTENSION-BOOKS))
