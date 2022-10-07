
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.temporary)





;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:information
           {:en "Information"
            :hu "Információ"}
           :informations
           {:en "Informations"
            :hu "Információk"}
           :other
           {:en "Other"
            :hu "Egyéb"}
           :invalid-number
           {:en "Invalid number"
            :hu "Érvénytelen szám"}
           :validity
           {:en "Validity"
            :hu "Érvényesség"}
           :index
           {:en "Index"
            :hu "Sorszám"}
           :count
           {:en "Count"
            :hu "Darabszám"}
           :quantity
           {:en "Quantity"
            :hu "Mennyiség"}

           :expiration-date
           {:en "Expiration date"
            :hu "Lejárat dátuma"}
           :release-date
           {:en "Release date"
            :hu "Kiadás dátuma"}
           :release-year
           {:en "Release year"
            :hu "Kiadás éve"}
           :released
           {:en "Released"
            :hu "Kiadva"}
           :issue-year
           {:en "Issue year"
            :hu "Kibocsátás éve"}
           :valid-until
           {:en "Valid until"
            :hu "Érvényesség"}
           :validity-date
           {:en "Validity date"
            :hu "Érvényesség dátuma"}
           :validity-interval
           {:en "Validity interval"
            :hu "Érvényesség időtartama"}
           :validity-interval-day
           {:en "Validity interval (day)"
            :hu "Érvényesség időtartama (nap)"}







           :page
           {:en "Page"
            :hu "Oldal"}
           :pages
           {:en "Pages"
            :hu "Oldalak"}

           :tag
           {:en "Tag"
            :hu "Címke"}
           :tags
           {:en "Tags"
            :hu "Címkék"}
           :no-tag-selected
           {:en "No tag selected"
            :hu "Nincs kiválasztott címke"}
           :no-tags-selected
           {:en "No tags selected"
            :hu "Nincsenek kiválasztott címkék"}
           :result
           {:en "Result"
            :hu "Eredmény"}
           :tools
           {:en "Tools"
            :hu "Eszközök"}
           :unnamed-group
           {:en "Unnamed group"
            :hu "Névtelen csoport"}

           :details
           {:en "Details"
            :hu "Részletek"}
           :data
           {:en "Data"
            :hu "Adatok"}
           :overview
           {:en "Overview"
            :hu "Áttekintés"}
           :link
           {:en "Link"
            :hu "Hivatkozás"}
           :link-label
           {:en "Link label"
            :hu "Hivatkozás címke"}
           :public-link
           {:en "Public link"
            :hu "Nyilvános hivatkozás"}
           :open-link!
           {:en "Open link"
            :hu "Hivatkozás megnyitása"}
           :open-in-new-page!
           {:en "Open in new page"
            :hu "Megnyitás új oldalon"}
           :open-in-new-tab!
           {:en "Open in new tab"
            :hu "Megnyitás új lapon"}



           :size
           {:en "Size"
            :hu "Méret"}
           :size-n
           {:en "Size: %"
            :hu "Méret: %"}

           :appearance
           {:en "Appearance"
            :hu "Megjelenés"}

           :active
           {:en "Active"
            :hu "Aktív"}

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

           :do-not-show-again!
           {:en "Do not show again"
            :hu "Ne mutassa többet"}

           :extensions
           {:en "Extensions"
            :hu "Eszközök"}

           :more-settings
           {:en "More settings"
            :hu "További beállítások"}
           :more-items
           {:en "More items"
            :hu "További tételek"}
           :less-options
           {:en "Less options"
            :hu "Kevesebb lehetőség"}
           :more-options
           {:en "More options"
            :hu "További lehetőségek"}
           :more-content
           {:en "More content"
            :hu "További tartalom"}
           :more-values
           {:en "More values"
            :hu "További értékek"}
           :more-data
           {:en "More data"
            :hu "További adatok"}
           :more-details
           {:en "More details"
            :hu "További részletek"}
           :more-actions
           {:en "More actions"
            :hu "További műveletek"}
           :more-info
           {:en "More info"
            :hu "További információ"}
           :basic-data
           {:en "Basic data"
            :hu "Alapvető adatok"}

           :billing-data
           {:en "Billing data"
            :hu "Számlázási adatok"}
           :technical-data
           {:en "Technical data"
            :hu "Technikai adatok"}
           :company-data
           {:en "Company data"
            :hu "Cég adatok"}

           :tutorial
           {:en "Tutorial"
            :hu "Bevezető"}
           :browser
           {:en "Browser"
            :hu "Böngésző"}
           :category
           {:en "Category"
            :hu "Kategória"}
           :categories
           {:en "Categories"
            :hu "Kategóriák"}
           :copy
           {:en "Copy"
            :hu "Másolat"}

           :description
           {:en "Description"
            :hu "Leírás"}

           :draft
           {:en "Draft"
            :hu "Vázlat"}
           :label
           {:en "Label"
            :hu "Címke"}
           :packages
           {:en "Packages"
            :hu "Csomagok"}
           :sample
           {:en "Sample"
            :hu "Minta"}
           :subcategory
           {:en "Subcategory"
            :hu "Alkategória"}
           :subcategories
           {:en "Subcategories"
            :hu "Alkategóriák"}



           :unkown
           {:en "Unkown"
            :hu "Ismeretlen"}

           :add-new!
           {:en "Add new"
            :hu "Hozzáadás"}

           
           :add-title!
           {:en "Add title"
            :hu "Cím hozzáadása"}

           :make-a-copy!
           {:en "Make a copy"
            :hu "Másolat készítése"}
           :view-copy!
           {:en "View copy"
            :hu "Másolat megtekintése"}

           :new
           {:en "New"
            :hu "Új"}

           :undo-delete!
           {:en "Undo delete"
            :hu "Törlés visszavonása"}

           :reorder
           {:en "Reorder"
            :hu "Sorrend"}
           :save-order!
           {:en "Save order"
            :hu "Sorrend mentése"}

           :dimensions
           {:en "Dimensions"
            :hu "Méretek"}
           :dimensions-wlh
           {:en "Dimensions [WxLxH]"
            :hu "Méretek [Sz, H, M]"}
           :outer-dimensions
           {:en "Outer dimensions"
            :hu "Külső méretek"}
           :outer-dimensions-wlh
           {:en "Outer dimensions [WxLxH]"
            :hu "Külső méretek [Sz, H, M]"}
           :inner-dimensions
           {:en "Inner dimensions"
            :hu "Belső méretek"}
           :inner-dimensions-wlh
           {:en "Inner dimensions [WxLxH]"
            :hu "Belső méretek [Sz, H, M]"}

           :type
           {:en "Type"
            :hu "Típus"}
           :types
           {:en "Types"
            :hu "Típusok"}



           :chart
           {:en "Chart"
            :hu "Grafikon"}
           :total-balance-n
           {:en "Total balance (%)"
            :hu "Teljes egyenleg (%)"}})
