
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.29
; Description:
; Version: v0.5.0
; Compatibility: x4.3.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-fruits.io
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.io     :as io]
              [mid-fruits.map    :as map]
              [mid-fruits.pretty :as pretty]
              [mid-fruits.reader :as reader]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def FILE-DOES-NOT-EXIST-ERROR "File does not exist: ")



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; clojure.java.io
(def file clojure.java.io/file)

; x.mid-utils.io
(def B->KB                   io/B->KB)
(def B->MB                   io/B->MB)
(def KB->B                   io/KB->B)
(def KB->MB                  io/KB->MB)
(def MB->B                   io/MB->B)
(def MB->KB                  io/MB->KB)
(def mime-types              io/mime-types)
(def extensions              io/extensions)
(def mime-type->extension    io/mime-type->extension)
(def extension->mime-type    io/extension->mime-type)
(def unknown-mime-type?      io/unknown-mime-type?)
(def image-extensions        io/image-extensions)
(def extension->image?       io/extension->image?)
(def mime-type->image?       io/mime-type->image?)
(def filepath->filename      io/filepath->filename)
(def filepath->extension     io/filepath->extension)
(def filename->extension     io/filename->extension)
(def filepath->basepath      io/filepath->basepath)
(def filename->basename      io/filename->basename)
(def filepath->basename      io/filepath->basename)
(def filepath->mime-type     io/filepath->mime-type)
(def filename->mime-type     io/filename->mime-type)
(def filepath->image?        io/filepath->image?)
(def filename->image?        io/filename->image?)
(def filename-valid?         io/filename-valid?)
(def filename-invalid?       io/filename-invalid?)
(def directory-name-valid?   io/directory-name-valid?)
(def directory-name-invalid? io/directory-name-invalid?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-exists?
  ; @param (string) filepath
  ;
  ; @return (boolean)
  [filepath]
  (let [file (file filepath)]
       (boolean (and (.exists file)
                     (not (.isDirectory file))))))

(defn get-filesize
  ; @param (string) filepath
  ;
  ; @return (integer)
  ;  The length of the file in bytes
  [filepath]
  (if (file-exists? filepath)
      (.length (file filepath))
      (println FILE-DOES-NOT-EXIST-ERROR filepath)))

(defn max-filesize-reached?
  ; @param (string) filepath
  ; @param (integer) max-filesize
  ;  The length of the file in bytes
  ;
  ; @return (boolean)
  [filepath max-filesize]
  (let [filesize (get-filesize filepath)]
       (>= filesize max-filesize)))

(defn delete-file!
  ; @param (string) filepath
  ;
  ; @return (?)
  [filepath]
  (if (file-exists? filepath)
      (try (clojure.java.io/delete-file filepath)
           (catch Exception e (str "Error while deleting: ")))
      (println FILE-DOES-NOT-EXIST-ERROR filepath)))

(defn copy-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) source-filepath
  ; @param (string) destination-filepath
  ;
  ; @return (?)
  [source-filepath destination-filepath]
  (if (file-exists? source-filepath)
      (try (clojure.java.io/copy (clojure.java.io/file source-filepath)
                                 (clojure.java.io/file destination-filepath))
           (catch Exception e (str "Error while copying: ")))
      (println FILE-DOES-NOT-EXIST-ERROR source-filepath)))

(defn read-file
  ; @param (string) filepath
  ;
  ; @return (string)
  [filepath]
  (if (file-exists? filepath)
      (slurp filepath)
      (println FILE-DOES-NOT-EXIST-ERROR filepath)))

(defn write-file!
  ; @param (string) filepath
  ; @param (*) content
  ;
  ; @return (?)
  [filepath content]
  (if (file-exists? filepath)
      (spit filepath (str content))
      (println FILE-DOES-NOT-EXIST-ERROR filepath)))

(defn append-to-file!
  ; @param (string) filepath
  ; @param (*) content
  ;
  ; @return (?)
  [filepath content])
  ; TODO

(defn create-file!
  ; @param (string) filepath
  ;
  ; @return (?)
  [filepath])
  ; TODO

(defn copy-uri-to-file!
  ; @param (string) uri
  ; @param (?) file
  ;
  ; @return (?)
  [uri file]
  (try (with-open [input  (clojure.java.io/input-stream  uri)
                   output (clojure.java.io/output-stream file)]
                  (clojure.java.io/copy input output))
       (catch Exception e (str "Error while copying: "))))



;; -- Directory ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory?
  ; @param (string) directory-path
  ;
  ; @return (boolean)
  [directory-path]
  (boolean (.isDirectory (file directory-path))))

(defn directory-exists?
  ; @param (string) directory-path
  ;
  ; @return (boolean)
  [directory-path]
  (let [directory (file directory-path)]
       (boolean (and (.exists directory)
                     (.isDirectory directory)))))

(defn create-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (try (.mkdir (java.io.File. directory-path))
       (catch Exception e (str "Error while creating directory: "))))

(defn file-list
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/file-list "my-directory")
  ;  => ["my-directory/my-file.ext" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (let [directory (file     directory-path)
        file-seq  (file-seq directory)]
       (mapv str (filter #(and (.isFile %)
                               (not (.isHidden %)))
                          (param file-seq)))))

(defn item-list
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/item-list "my-directory")
  ;  => ["my-directory/my-file.ext" "my-directory/my-subdirectory" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (vector/remove-item (mapv str (file-seq (file directory-path)))
                      (param directory-path)))

(defn empty-directory?
  ; @param (string) directory-path
  ;
  ; @return (boolean)
  [directory-path]
  (empty? (item-list directory-path)))

(defn delete-empty-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (delete-file! directory-path))

(defn delete-items-in-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (doseq [item-path (item-list directory-path)]
         (if (directory? item-path)
             (do (delete-items-in-directory! item-path)
                 (delete-empty-directory!    item-path))
             (delete-file! item-path))))

(defn delete-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (delete-items-in-directory! directory-path)
  (delete-empty-directory!    directory-path))



;; -- EDN files ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn write-edn-file!
  ; @param (string) filepath
  ; @param (*) content
  ; @param (map) options
  ;  {:abc? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  (io/write-edn-file! "my-file.edn" {...})
  ;
  ; @return (?)
  [filepath content & [options]]
  (let [output (pretty/mixed->string content options)]
       (write-file! filepath output)))

(defn read-edn-file
  ; @param (string) filepath
  ;
  ; @return (map)
  [filepath]
  (let [file-content (read-file filepath)]
       (if (some? (string/trim file-content))
           (reader/string->mixed file-content))))

(defn swap-edn-file!
  ; @param (string) filepath
  ; @param (function) f
  ; @param (*) params
  ;
  ; @usage
  ;  (io/swap-edn-file! "my-file.edn" assoc-in [:items :xyz] "XYZ")
  ;
  ; @usage
  ;  (io/swap-edn-file! "my-file.edn" vector/conj-item "XYZ")
  ;
  ; @return (nil)
  [filepath f & params]
  (let [edn    (read-edn-file filepath)
        output (apply f (vector/cons-item params edn))]
       (write-edn-file! filepath output)))
