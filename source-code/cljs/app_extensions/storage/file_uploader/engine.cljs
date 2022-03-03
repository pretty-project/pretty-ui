
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.file-uploader.engine
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.keyword :as keyword]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  ; A file-uploader által indított kéréseket egyedi azonosítóval szükséges ellátni,
  ; hogy egyszerre párhuzamosan több fájlfeltöltési folyamat is futtatható legyen!
  (keyword/add-namespace :storage uploader-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

;`(storage.file-uploader/upload-files!)
