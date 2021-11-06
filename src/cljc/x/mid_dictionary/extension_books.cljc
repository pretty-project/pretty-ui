
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
       :hu "Műveletek"}})

(def calendar
     {:calendar
      {:en "Calendar"
       :hu "Naptár"}})

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
       :hu "Ügyfél szerkesztése"}})

(def charts
     {:charts
      {:en "Charts"
       :hu "Charts"}})

(def devices
     {:devices
      {:en "Devices"
       :hu "Eszközök"}})

(def employees
     {:employees
      {:en "Employees"
       :hu "Alkamazottak"}})

(def inventories
     {:inventories
      {:en "Inventories"
       :hu "Raktárkészlet"}})

(def jobs
     {:jobs
      {:en "Jobs"
       :hu "Munkák"}})

(def machines
     {:machines
      {:en "Machines"
       :hu "Gépek"}})

(def media
     {:available-capacity-in-storage-is
      {:en ""
       :hu "A rendelkezésre álló kapacitás a tárhelyen: % MB"}
      :cant-attach-more-files
      {:en "You can not select more files!"
       :hu "Nem választható ki több fájl!"}
      :file-storage
      {:en "File storage"
       :hu "Tárhely"}
      :max-uploading-size-is
      {:en ""
       :hu "Az egyszerre feltölthető fájlok mérete: max. % MB"}
      :media
      {:en "Storage"
       :hu "Tárhely"}
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
     {:price-quote
      {:en "Price quote"
       :hu "Árajánlat"}
      :price-quotes
      {:en "Price quotes"
       :hu "Árajánlatok"}})

(def products
     {:product
      {:en "Product"
       :hu "Termék"}
      :products
      {:en "Products"
       :hu "Termékek"}})

(def services
     {:services
      {:en "Services"
       :hu "Szolgáltatások"}})

(def vehicles
     {:vehicles
      {:en "Vehicles"
       :hu "Járművek"}})

(def websites
     {:website-configuration
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
                            vehicles
                            websites))
