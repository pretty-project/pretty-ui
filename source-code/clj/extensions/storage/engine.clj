
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.engine
    (:require [mid-fruits.candy :refer [param return]]
              [server-fruits.io :as io]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-id->filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) file-id
  ; @param (string) filename
  ;
  ; @example
  ;  (engine/file-id->filename "my-item" "my-image.png")
  ;  =>
  ;  "my-item.png"
  ;
  ; @return (string)
  [file-id filename]
  (if-let [extension (io/filename->extension filename)]
          (str    file-id "." extension)
          (return file-id)))
