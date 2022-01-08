
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v0.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.io
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.regex  :refer [re-match? re-mismatch?]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name filepath
;  "my-directory/my-file.ext"
;
; @name filename
;  "my-file.ext"
;
; @name extension
;  ".ext"
;
;  Ha egy fájl neve egyetlen "." karaktert tartalmaz és az a fájl nevének első
;  karaktere, akkor a "." karakter után következő rész a fájl nevének tekintendő,
;  nem pedig a fájl kiterjesztésének!
;  Egyes operációs rendszerek a "." karakterrel kezdődő fájlnevű fájlokat rejtett
;  fájloknak tekintik.
;
; @name basename
;  "my-file"
;
; @name basepath
;  "my-directory"



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def FILENAME-PATTERN #"^[a-zA-Z0-9àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ._ -]+$")

; @constant (string)
(def DIRECTORY-NAME-PATTERN #"^[a-zA-Z0-9àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ._ -]+$")

; @constant (integer)
(def MAX-FILENAME-LENGTH 32)

; @constant (map)
(def MIME-TYPES {"aac"  "audio/aac"
                 "avi"  "video/x-msvideo"
                 "bin"  "application/octet-stream"
                 "bmp"  "image/bmp"
                 "bz"   "application/x-bzip"
                 "bz2"  "application/x-bzip2"
                 "css"  "text/css"
                 "doc"  "application/msword"
                 "docx" "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                 "gif"  "image/gif"
                 "htm"  "text/html"
                 "html" "text/html"
                 "ico"  "image/vnd.microsoft.icon"
                 "jar"  "application/java-archive"
                 "jpg"  "image/jpeg"
                 "jpeg" "image/jpeg"
                 "js"   "text/javascript"
                 "mpeg" "video/mpeg"
                 "mp3"  "audio/mpeg"
                 "mp4"  "video/mp4"
                 "m4a"  "audio/m4a"
                 "m4v"  "video/mp4"
                 "odp"  "application/vnd.oasis.opendocument.presentation"
                 "ods"  "application/vnd.oasis.opendocument.spreadsheet"
                 "odt"  "application/vnd.oasis.opendocument.text"
                 "otf"  "font/otf"
                 "png"  "image/png"
                 "pdf"  "application/pdf"
                 "ppt"  "application/vnd.ms-powerpoint"
                 "rar"  "application/x-rar-compressed"
                 "rtf"  "application/rtf"
                 "svg"  "image/svg+xml"
                 "tar"  "application/x-tar"
                 "tif"  "image/tiff"
                 "tiff" "image/tiff"
                 "ttf"  "font/ttf"
                 "txt"  "text/plain"
                 "wav"  "audio/wav"
                 "weba" "audio/webm"
                 "webm" "video/webm"
                 "webp" "image/webp"
                 "xml"  "text/xml"
                 "xls"  "application/vnd.ms-excel"
                 "xlsx" "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                 "zip"  "application/zip"
                 "7z"   "application/x-7z-compressed"})

; @constant (map)
(def EXTENSIONS (map/swap MIME-TYPES))

; @constant (strings in vector)
;  A rendszer által ismert képformátumok. A lista tetszés szerint bővíthető.
(def IMAGE-EXTENSIONS ["jpg" "jpeg" "png"])



;; -- Filesize ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn B->KB  [n] (/ n 1000))
(defn B->MB  [n] (/ n 1000000))
(defn B->GB  [n] (/ n 1000000000))
(defn KB->B  [n] (* n 1000))
(defn KB->MB [n] (/ n 1000))
(defn KB->GB [n] (/ n 1000000))
(defn MB->B  [n] (* n 1000000))
(defn MB->KB [n] (* n 1000))
(defn MB->GB [n] (/ n 1000))



;; -- MIME types --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mime-type->extension
  ; @param (string) mime-type
  ;
  ; @example
  ;  (io/mime-type->extension "text/xml")
  ;  =>
  ;  "xml"
  ;
  ; @example
  ;  (io/mime-type->extension "foo/bar")
  ;  =>
  ;  "unknown"
  ;
  ; @return (string)
  [mime-type]
  (get EXTENSIONS (string/lowercase mime-type) "unknown"))

(defn extension->mime-type
  ; @param (extension)
  ;
  ; @example
  ;  (io/extension->mime-type "xml")
  ;  =>
  ;  "text/xml"
  ;
  ; @example
  ;  (io/extension->mime-type "bar")
  ;  =>
  ;  "unknown/unknown"
  ;
  ; @return (string)
  [extension]
  (get MIME-TYPES (string/lowercase extension) "unknown/unknown"))

(defn unknown-mime-type?
  ; @param (string) mime-type
  ;
  ; @example
  ;  (io/unknown-mime-type? "text/xml")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (io/unknown-mime-type? "foo/bar")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [mime-type]
  (nil? (mime-type->extension mime-type)))



;; -- File --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extension->image?
  ; @param (string) extension
  ;
  ; @example
  ;  (io/extension->image? "png")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [extension]
  (vector/contains-item? IMAGE-EXTENSIONS extension))

(defn mime-type->image?
  ; @param (string) extension
  ;
  ; @example
  ;  (io/mime-type->image? "image/png")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [mime-type]
  (string/starts-with? mime-type "image"))

(defn filepath->filename
  ; @param (string) filepath
  ;
  ; @example
  ;  (io/filepath->filename "a/b.png")
  ;  =>
  ;  "b.png"
  ;
  ; @return (string)
  [filepath]
  (if-let [filename (string/after-last-occurence filepath "/")]
          (return filename)
          (return filepath)))

(defn filepath->extension
  ; @param (string) filepath
  ;
  ; @example
  ;  (io/filepath->extension "a/b.PNG")
  ;  =>
  ;  "png"
  ;
  ; @example
  ;  (io/filepath->extension "a/.hidden-file.txt")
  ;  =>
  ;  "txt"
  ;
  ; @example
  ;  (io/filepath->extension "a/.hidden-file")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [filepath]
  (let [filename (-> filepath filepath->filename (string/not-starts-with! "."))]
       (if-let [extension (string/after-last-occurence filename ".")]
               (string/lowercase extension)
               (return nil))))

(defn filename->extension
  ; @param (string) filename
  ;
  ; @return (string)
  [filename]
  (filepath->extension filename))

(defn filepath->basepath
  ; @param (string) filepath
  ;
  ; @example
  ;  (io/filepath->basepath "a/b.png")
  ;  =>
  ;  "a"
  ;
  ; @example
  ;  (io/filepath->basepath "a/b/c.png")
  ;  =>
  ;  "a/b"
  ;
  ; @example
  ;  (io/filepath->basepath "c.png")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [filepath]
  (string/before-last-occurence filepath "/"))

(defn filename->basename
  ; @param (string) filename
  ;
  ; @example
  ;  (io/filename->basename "b.png")
  ;  =>
  ;  "b"
  ;
  ; @example
  ;  (io/filename->basename ".hidden-file.txt")
  ;  =>
  ;  ".hidden-file"
  ;
  ; @example
  ;  (io/filename->basename ".hidden-file")
  ;  =>
  ;  ".hidden-file"
  ;
  ; @return (string)
  [filename]
  (if-let [extension (filename->extension filename)]
          (string/before-last-occurence filename (str "." extension))
          (return filename)))

(defn filepath->basename
  ; @param (string) filepath
  ;
  ; @example
  ;  (io/filepath->basename "a/b.png")
  ;  =>
  ;  "b"
  ;
  ; @example
  ;  (io/filepath->basename "a/.hidden-file.txt")
  ;  =>
  ;  ".hidden-file"
  ;
  ; @example
  ;  (io/filepath->basename "a/.hidden-file")
  ;  =>
  ;  ".hidden-file"
  ;
  ; @return (string)
  [filepath]
  (-> filepath filepath->filename filename->basename))

(defn filepath->mime-type
  ; @param (string) filepath
  ;
  ; @example
  ;  (io/filepath->mime-type "a/b.png")
  ;  =>
  ;  "image/png"
  ;
  ; @example
  ;  (io/filepath->mime-type "a/b")
  ;  =>
  ;  "unknown/unknown"
  ;
  ; @return (string)
  [filepath]
  (-> filepath filepath->extension extension->mime-type))

(defn filename->mime-type
  ; @param (string) filename
  ;
  ; @return (string)
  [filename]
  (filepath->mime-type filename))

(defn filepath->image?
  ; @param (string) filepath
  ;
  ; @usage
  ;  (io/filepath->image? "a/b.png")
  ;
  ; @return (boolean)
  [filepath]
  (-> filepath filepath->extension extension->image?))

(defn filename->image?
  ; @param (string) filename
  ;
  ; @return (boolean)
  [filename]
  (filepath->image? filename))



;; -- Validators ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filename-valid?
  ; @param (string) filename
  ;
  ; @return (boolean)
  [filename]
  (re-match? filename FILENAME-PATTERN))

(defn filename-invalid?
  ; @param (string) filename
  ;
  ; @return (boolean)
  [filename]
  (re-mismatch? filename FILENAME-PATTERN))

(defn directory-name-valid?
  ; @param (string) directory-name
  ;
  ; @return (boolean)
  [directory-name]
  (re-match? directory-name DIRECTORY-NAME-PATTERN))

(defn directory-name-invalid?
  ; @param (string) directory-name
  ;
  ; @return (boolean)
  [directory-name]
  (re-mismatch? directory-name DIRECTORY-NAME-PATTERN))
