
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.04
; Description:
; Version: v0.2.0
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.extension-books)



;; -- Extension books ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(def actions
     {:actions
      {:en "Actions"
       :hu "Műveletek"}
      :add-action
      {:en "Add action"
       :hu "Művelet hozzáadása"}
      :edit-action
      {:en "Edit action"
       :hu "Művelet szerkesztése"}
      :new-action
      {:en "New action"
       :hu "Új művelet"}
      :unnamed-action
      {:en "Unnamed action"
       :hu "Névtelen művelet"}})

(def calendar
     {:add-calendar
      {:en "Add calendar"
       :hu "Naptár hozzáadása"}
      :calendar
      {:en "Calendar"
       :hu "Naptár"}
      :edit-calendar
      {:en "Edit calendar"
       :hu "Naptár szerkesztése"}
      :new-calendar
      {:en "New calendar"
       :hu "Új Naptár"}
      :unnamed-calendar
      {:en "Unnamed calendar"
       :hu "Névtelen naptár"}})

(def clients
     {:add-client
      {:en "Add client"
       :hu "Ügyfél hozzáadása"}
      :clients
      {:en "Clients"
       :hu "Ügyfelek"}
      :client-id
      {:en "Client ID"
       :hu "Ügyfél azonosító"}
      :edit-client
      {:en "Edit client"
       :hu "Ügyfél szerkesztése"}
      :new-client
      {:en "New client"
       :hu "Új ügyfél"}
      :unnamed-client
      {:en "Unnamed client"
       :hu "Névtelen ügyfél"}})

(def charts
     {:add-chart
      {:en "Add chart"
       :hu "Chart hozzáadása"}
      :charts
      {:en "Charts"
       :hu "Charts"}
      :edit-chart
      {:en "Edit chart"
       :hu "Chart szerkesztése"}
      :new-chart
      {:en "New chart"
       :hu "Új chart"}
      :unnamed-chart
      {:en "Unnamed chart"
       :hu "Névtelen chart"}})

(def devices
     {:add-device
      {:en "Add device"
       :hu "Eszköz hozzáadása"}
      :edit-device
      {:en "Edit device"
       :hu "Eszköz szerkesztése"}
      :devices
      {:en "Devices"
       :hu "Eszközök"}
      :new-device
      {:en "New device"
       :hu "Új eszköz"}
      :unnamed-device
      {:en "Unnamed device"
       :hu "Névtelen eszköz"}})

(def employees
     {:add-employee
      {:en "Add employee"
       :hu "Alkalmazott hozzáadása"}
      :edit-employee
      {:en "Edit employee"
       :hu "Alkalmazott szerkesztése"}
      :employees
      {:en "Employees"
       :hu "Alkamazottak"}
      :new-employee
      {:en "New employee"
       :hu "Új alkalmazott"}
      :unnamed-employee
      {:en "Unnamed employee"
       :hu "Névtelen alkalmazott"}})

(def inventories
     {:add-inventory
      {:en "Add inventory"
       :hu "Raktár hozzáadása"}
      :edit-inventory
      {:en "Edit inventory"
       :hu "Raktár szerkesztése"}
      :inventories
      {:en "Inventories"
       :hu "Raktárkészlet"}
      :new-inventory
      {:en "New inventory"
       :hu "Új raktár"}
      :unnamed-inventory
      {:en "Unnamed inventory"
       :hu "Névtelen raktár"}})

(def jobs
     {:add-job
      {:en "Add job"
       :hu "Munka hozzáadása"}
      :edit-job
      {:en "Edit job"
       :hu "Munka szerkesztése"}
      :jobs
      {:en "Jobs"
       :hu "Munkák"}
      :new-action
      {:en "New job"
       :hu "Új munka"}
      :unnamed-job
      {:en "Unnamed job"
       :hu "Névtelen munka"}})

(def machines
     {:add-machine
      {:en "Add machine"
       :hu "Gép hozzáadása"}
      :edit-machine
      {:en "Edit machine"
       :hu "Gép szerkesztése"}
      :machines
      {:en "Machines"
       :hu "Gépek"}
      :new-action
      {:en "New machine"
       :hu "Új gép"}
      :unnamed-machine
      {:en "Unnamed machine"
       :hu "Névtelen gép"}})

(def media
     {:add-directory
      {:en "Add directory"
       :hu "Mappa hozzáadása"}
      :add-file
      {:en "Add file"
       :hu "Fájl hozzáadása"}
      :available-capacity-in-storage-is
      {:en ""
       :hu "A rendelkezésre álló kapacitás a tárhelyen: % MB"}
      :cant-attach-more-files
      {:en "You can not select more files!"
       :hu "Nem választható ki több fájl!"}
      :edit-directory
      {:en "Edit directory"
       :hu "Mappa szerkesztése"}
      :edit-file
      {:en "Edit file"
       :hu "Fájl szerkesztése"}
      :file-storage
      {:en "File storage"
       :hu "Tárhely"}
      :max-uploading-size-is
      {:en ""
       :hu "Az egyszerre feltölthető fájlok mérete: max. % MB"}
      :media
      {:en "Storage"
       :hu "Tárhely"}
      :new-directory
      {:en "New directory"
       :hu "Új mappa"}
      :new-file
      {:en "New file"
       :hu "Új fájl"}
      :unnamed-directory
      {:en "Unnamed directory"
       :hu "Névtelen mappa"}
      :unnamed-file
      {:en "Unnamed file"
       :hu "Névtelen fájl"}
      :uploading-size-is
      {:en ""
       :hu "A feltölteni kívánt fájlok mérete: %1 MB / %2 MB"}
      :my-storage
      {:en "My storage"
       :hu "Saját tárhely"}
      :search-in-storage
      {:en "Search in storage"
       :hu "Keresés a tárhelyen"}
      :will-be-deleted-after
      {:en ""
       :hu "A lomtárba helyezett elemek % nap után véglegesen törlődnek."}})

(def price-quotes
     {:add-price-quote
      {:en "Add price quote"
       :hu "Árajánlat hozzáadása"}
      :edit-price-quote
      {:en "Edit price quote"
       :hu "Árajánlat szerkesztése"}
      :new-price-quote
      {:en "New price quote"
       :hu "Új árajánlat"}
      :price-quote
      {:en "Price quote"
       :hu "Árajánlat"}
      :price-quotes
      {:en "Price quotes"
       :hu "Árajánlatok"}
      :unnamed-price-quote
      {:en "Unnamed price quote"
       :hu "Névtelen árajánlat"}})

(def products
     {:add-product
      {:en "Add product"
       :hu "Termék hozzáadása"}
      :edit-product
      {:en "Edit product"
       :hu "Termék szerkesztése"}
      :new-product
      {:en "New product"
       :hu "Új termék"}
      :product
      {:en "Product"
       :hu "Termék"}
      :products
      {:en "Products"
       :hu "Termékek"}
      :unnamed-product
      {:en "Unnamed product"
       :hu "Névtelen termék"}})

(def services
     {:add-service
      {:en "Add service"
       :hu "Szolgáltatás hozzáadása"}
      :edit-service
      {:en "Edit service"
       :hu "Szolgáltatás szerkesztése"}
      :new-service
      {:en "New service"
       :hu "Új szolgáltatás"}
      :services
      {:en "Services"
       :hu "Szolgáltatások"}
      :unnamed-service
      {:en "Unnamed service"
       :hu "Névtelen szolgáltatás"}})

(def storage
     {:storage
      {:en "Storage"
       :hu "Tárhely"}})

(def vehicles
     {:add-vehicle
      {:en "Add vehicle"
       :hu "Jármű hozzáadása"}
      :edit-vehicle
      {:en "Edit vehicle"
       :hu "Jármű szerkesztése"}
      :new-vehicle
      {:en "New vehicle"
       :hu "Új jármű"}
      :unnamed-vehicle
      {:en "Unnamed vehicle"
       :hu "Névtelen jármű"}
      :vehicles
      {:en "Vehicles"
       :hu "Járművek"}})

(def websites
     {:add-website
      {:en "Add website"
       :hu "Webhely hozzáadása"}
      :edit-website
      {:en "Edit website"
       :hu "Webhely szerkesztése"}
      :new-website
      {:en "New website"
       :hu "Új webhely"}
      :unnamed-website
      {:en "Unnamed website"
       :hu "Névtelen webhely"}
      :website-configuration
      {:en "Website configuration"
       :hu "Webhely beállításai"}
      :website-menu
      {:en "Website menu"
       :hu "Webhely menu"}
      :websites
      {:en "Websites"
       :hu "Weboldalak"}})



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def EXTENSION-BOOKS (merge actions
                            calendar
                            clients
                            charts
                            devices
                            employees
                            inventories
                            jobs
                            machines
                            media
                            price-quotes
                            products
                            services
                            storage
                            vehicles
                            websites))
