
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.helpers
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.io      :as io]
              [mid-fruits.string  :as string]
              [x.app-media.api    :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  ; A file-uploader által indított kéréseket egyedi azonosítóval szükséges ellátni,
  ; hogy egyszerre párhuzamosan több fájlfeltöltési folyamat is futtatható legyen!
  (keyword/add-namespace :storage.file-uploader uploader-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn uploader-props->allowed-extensions-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [allowed-extensions]}]
  (let [allowed-extensions (or allowed-extensions (media/allowed-extensions))]
       (str "." (string/join allowed-extensions ", ."))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item->thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias filename]}]
  {:icon :insert_drive_file
   :uri (if (io/filename->image? alias) filename)})
