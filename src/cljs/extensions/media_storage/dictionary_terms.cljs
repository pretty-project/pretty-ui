
(ns extensions.media-storage.dictionary-terms
    (:require [x.app-core.api :as a]))



;; -- Books -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def TERMS
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
       :hu "A lomtárba helyezett elemek 60 nap után véglegesen törlődnek."}})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-dictionary/add-terms! TERMS]})
