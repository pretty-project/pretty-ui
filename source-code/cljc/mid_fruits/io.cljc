
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v0.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.io
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.loop    :refer [reduce-while]]
              [mid-fruits.map     :as map]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
(def MAX-FILENAME-LENGTH 32)

; @constant (vector)
(def FILENAME-INVALID-CHARACTERS
     ; TODO ...
     ["@" "?" "/" "#" "&" "\\"])



;; -- Filesize ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn B->KB
  ; @param (B) filesize
  ;
  ; @example
  ;  (io/B->KB 256)
  ;  =>
  ;  0.26
  ;
  ; @example
  ;  (io/B->KB 65536)
  ;  =>
  ;  65.54
  ;
  ; @example
  ;  (io/B->KB 1048576)
  ;  =>
  ;  1048.58
  ;
  ; @return (KB)
  [filesize]
  (.toFixed (/ filesize 1000) 2))

(defn B->MB
  ; @param (B) filesize
  ;
  ; @example
  ;  (io/B->MB 256)
  ;  =>
  ;  0.00
  ;
  ; @example
  ;  (io/B->MB 65536)
  ;  =>
  ;  0.07
  ;
  ; @example
  ;  (io/B->MB 1048576)
  ;  =>
  ;  1.05
  ;
  ; @return (MB)
  [filesize]
  (.toFixed (/ filesize 1000000) 2))

(defn B->GB
  ; @param (B) filesize
  ;
  ; @example
  ;  (io/B->GB 256)
  ;  =>
  ;  0.00
  ;
  ; @example
  ;  (io/B->GB 33554432)
  ;  =>
  ;  0.03
  ;
  ; @return (GB)
  [filesize]
  (.toFixed (/ filesize 1000000000) 2))

(defn KB->B
  ; @param (KB) filesize
  ;
  ; @example
  ;  (io/KB->B 10)
  ;  =>
  ;  10000
  ;
  ; @return (B)
  [filesize]
  (* filesize 1000))

(defn KB->MB
  ; @param (KB) filesize
  ;
  ; @example
  ;  (io/KB->MB 10000)
  ;  =>
  ;  10.00
  ;
  ; @return (MB)
  [filesize]
  (.toFixed (/ filesize 1000) 2))

(defn MB->B
  ; @param (MB) filesize
  ;
  ; @example
  ;  (io/MB->B 10)
  ;  =>
  ;  10000000
  ;
  ; @return (B)
  [filesize]
  (* filesize 1000000))

(defn MB->KB
  ; @param (MB) filesize
  ;
  ; @example
  ;  (io/MB->KB 10)
  ;  =>
  ;  10000
  ;
  ; @return (KB)
  [filesize]
  (* filesize 1000))

(defn MB->GB
  ; @param (MB) filesize
  ;
  ; @example
  ;  (io/MB->GB 400)
  ;  =>
  ;  0.4
  ;
  ; @return (GB)
  [filesize]
  (/ filesize 1000))



;; -- MIME types --------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def mime-types
  {"aac"  "audio/aac"
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
(def extensions (map/swap mime-types))

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
  (get extensions (string/lowercase mime-type)))

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
  (get mime-types (string/lowercase extension)
                  (param "unknown/unknown")))

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
  (let [extension (mime-type->extension mime-type)]
       (nil? extension)))



;; -- File --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def image-extensions
     ; A rendszer által ismert képformátumok. A lista tetszés szerint bővíthető.
     ["jpg" "jpeg" "png"])

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
  (vector/contains-item? image-extensions extension))

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
  ;  (io/filepath->extension "a/b.png")
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
  (let [filename (filepath->filename          filepath)
        filename (string/not-starts-with!     filename ".")]
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
          (let [trail (str "." extension)]
               (string/before-last-occurence filename trail))
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
  (let [filename (filepath->filename filepath)]
       (filename->basename filename)))

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
  (let [extension (filepath->extension filepath)]
       (extension->mime-type extension)))

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
  (let [extension (filepath->extension filepath)]
       (extension->image? extension)))

(defn filename->image?
  ; @param (string) filename
  ;
  ; @return (boolean)
  [filename]
  (filepath->image? filename))



;; -- Validators ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- filename-contains-invalid-characters?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  ;
  ; @return (boolean)
  [filename]
  (reduce-while (fn [%1 %2] (string/contains-part? filename %2))
                (param false)
                (param FILENAME-INVALID-CHARACTERS)
                (fn [%1 _] (boolean %1))))

(defn filename-valid?
  ; @param (string) filename
  ;
  ; @return (boolean)
  [filename]
  (boolean (and (string/nonempty? filename)
                (not (filename-contains-invalid-characters? filename)))))

(defn filename-invalid?
  ; @param (string) filename
  ;
  ; @return (boolean)
  [filename]
  (not (filename-valid? filename)))

(defn directory-name-valid?
  ; @param (string) directory-name
  ;
  ; @return (boolean)
  [directory-name]
  (filename-valid? directory-name))

(defn directory-name-invalid?
  ; @param (string) directory-name
  ;
  ; @return (boolean)
  [directory-name]
  (filename-invalid? directory-name))
