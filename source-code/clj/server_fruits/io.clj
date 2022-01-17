
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.29
; Description:
; Version: v0.7.8
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-fruits.io
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.io     :as io]
              [mid-fruits.pretty :as pretty]
              [mid-fruits.reader :as reader]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;  Hibaüzenet kiírásakor a fájl neve idézőjelekben legyen kiírva, hogy a nil
;  értékű fájlnevek is egyértelműek legyenek.
(def FILE-DOES-NOT-EXIST-ERROR "File does not exist:")



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; clojure.java.io
(def file clojure.java.io/file)

; x.mid-utils.io
(def MIME-TYPES              io/MIME-TYPES)
(def EXTENSIONS              io/EXTENSIONS)
(def IMAGE-EXTENSIONS        io/IMAGE-EXTENSIONS)
(def B->KB                   io/B->KB)
(def B->MB                   io/B->MB)
(def KB->B                   io/KB->B)
(def KB->MB                  io/KB->MB)
(def MB->B                   io/MB->B)
(def MB->KB                  io/MB->KB)
(def mime-type->extension    io/mime-type->extension)
(def extension->mime-type    io/extension->mime-type)
(def unknown-mime-type?      io/unknown-mime-type?)
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
       (and (-> file .exists)
            (-> file .isDirectory not))))

(defn file-not-exists?
  ; @param (string) filepath
  ;
  ; @return (boolean)
  [filepath]
  (let [file (file filepath)]
       (or (-> file .extists not)
           (-> file .isDirectory))))

(defn get-filesize
  ; @param (string) filepath
  ;
  ; @return (B)
  ;  The length of the file in bytes
  [filepath]
  (try (if (file-exists? filepath)
           (->           filepath file .length)
           (throw (Exception. FILE-DOES-NOT-EXIST-ERROR)))
      (catch Exception e (println (str e " \"" filepath "\"")))))

(defn max-filesize-reached?
  ; @param (string) filepath
  ; @param (B) max-filesize
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
  (try (if (file-exists? filepath)
           (clojure.java.io/delete-file filepath)
           (throw (Exception. FILE-DOES-NOT-EXIST-ERROR)))
      (catch Exception e (println (str e " \"" filepath "\"")))))

(defn copy-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) source-filepath
  ; @param (string) destination-filepath
  ;
  ; @return (?)
  [source-filepath destination-filepath]
  (try (if (file-exists? source-filepath)
           (clojure.java.io/copy (clojure.java.io/file source-filepath)
                                 (clojure.java.io/file destination-filepath))
           (throw (Exception. FILE-DOES-NOT-EXIST-ERROR)))
       (catch Exception e (println (str e " \"" source-filepath "\"")))))

(defn read-file
  ; @param (string) filepath
  ;
  ; @return (string)
  [filepath]
  (try (if (file-exists? filepath)
           (slurp        filepath)
           (throw (Exception. FILE-DOES-NOT-EXIST-ERROR)))
      (catch Exception e (println (str e " \"" filepath "\"")))))

(defn write-file!
  ; @param (string) filepath
  ; @param (*) content
  ;
  ; @return (?)
  [filepath content]
  (spit filepath (str content)))

(defn append-to-file!
  ; @param (string) filepath
  ; @param (*) content
  ; @param (map)(opt) options
  ;  {:max-line-count (integer)(opt)
  ;   :reverse? (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (?)
  [filepath content {:keys [max-line-count reverse?]}]
  (let [file-content (read-file filepath)
        output       (if reverse? (str content      "\n" file-content)
                                  (str file-content "\n" content))]
       (if (some? max-line-count)
           ; If maximum number of lines is limited ...
           (let [output (string/max-lines output max-line-count)]
                (write-file! filepath output))
           ; If maximum number of lines is NOT limited ...
           (write-file! filepath output))))

(defn create-file!
  ; @param (string) filepath
  ;
  ; @return (?)
  [filepath]
  (spit filepath nil))

(defn copy-uri-to-file!
  ; @param (string) uri
  ; @param (?) file
  ;
  ; @return (?)
  [uri file]
  (try (with-open [input  (clojure.java.io/input-stream  uri)
                   output (clojure.java.io/output-stream file)]
                  (clojure.java.io/copy input output))
       (catch Exception e (println e))))



;; -- Directory ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory?
  ; @param (string) directory-path
  ;
  ; @return (boolean)
  [directory-path]
  (-> directory-path file .isDirectory))

(defn directory-exists?
  ; @param (string) directory-path
  ;
  ; @return (boolean)
  [directory-path]
  (let [directory (file directory-path)]
       (and (.exists      directory)
            (.isDirectory directory))))

(defn create-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (try (-> directory-path java.io.File. .mkdir)
       (catch Exception e (println e))))

(defn file-list
  ; @description
  ;  A directory-path paraméterként átadott elérési útvonalon található
  ;  mappa fájljainak listája (egy mélységben).
  ;
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/file-list "my-directory")
  ;  =>
  ;  ["my-directory/my-file.ext" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (let [directory (file       directory-path)
        file-seq  (.listFiles directory)]
       (mapv str (filter #(and (-> % .isFile)
                               (-> % .isHidden not))
                          (param file-seq)))))

(defn all-file-list
  ; @description
  ;  A directory-path paraméterként átadott elérési útvonalon található
  ;  mappa fájljainak listája (több mélységben).
  ;
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/all-file-list "my-directory")
  ;  =>
  ;  ["my-directory/my-file.ext" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (let [directory (file     directory-path)
        file-seq  (file-seq directory)]
       (mapv str (filter #(and (-> % .isFile)
                               (-> % .isHidden not))
                          (param file-seq)))))

(defn subdirectory-list
  ; @description
  ;  A directory-path paraméterként átadott elérési útvonalon található
  ;  mappa almappáinak listája (egy mélységben).
  ;
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/subdirectory-list "my-directory")
  ;  =>
  ;  ["my-directory/my-subdirectory" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (let [directory (file       directory-path)
        file-seq  (.listFiles directory)]
       (mapv str (filter #(and (-> % .isDirectory)
                               (-> % .isHidden not))
                          (param file-seq)))))

(defn all-subdirectory-list
  ; @description
  ;  A directory-path paraméterként átadott elérési útvonalon található
  ;  mappa almappáinak listája (több mélységben).
  ;
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/all-subdirectory-list "my-directory")
  ;  =>
  ;  ["my-directory/my-subdirectory" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (let [directory (file     directory-path)
        file-seq  (file-seq directory)]
       (mapv str (filter #(and (-> % .isDirectory)
                               (-> % .isHidden not))
                          (param file-seq)))))

(defn item-list
  ; @description
  ;  A directory-path paraméterként átadott elérési útvonalon található
  ;  mappa elemeinek listája (egy mélységben).
  ;
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/item-list "my-directory")
  ;  =>
  ;  ["my-directory/my-file.ext" "my-directory/my-subdirectory" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (vector/remove-item (mapv str (-> directory-path file .listFiles))
                      (param directory-path)))

(defn all-item-list
  ; @description
  ;  A directory-path paraméterként átadott elérési útvonalon található
  ;  mappa elemeinek listája (több mélységben).
  ;
  ; @param (string) directory-path
  ;
  ; @example
  ;  (io/all-item-list "my-directory")
  ;  =>
  ;  ["my-directory/my-file.ext" "my-directory/my-subdirectory" ...]
  ;
  ; @return (strings in vector)
  [directory-path]
  (vector/remove-item (mapv str (-> directory-path file file-seq))
                      (param directory-path)))

(defn empty-directory?
  ; @param (string) directory-path
  ;
  ; @return (boolean)
  [directory-path]
  (-> directory-path item-list empty?))

(defn delete-empty-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (delete-file! directory-path))

(defn empty-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (doseq [item-path (item-list directory-path)]
         (if (directory? item-path)
             (do (empty-directory!        item-path)
                 (delete-empty-directory! item-path))
             (delete-file! item-path))))

(defn delete-directory!
  ; @param (string) directory-path
  ;
  ; @return (?)
  [directory-path]
  (empty-directory!        directory-path)
  (delete-empty-directory! directory-path))



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
  ; @return (map)
  [filepath content & [options]]
  (let [output (pretty/mixed->string content options)]
       (write-file! filepath (str "\n" output))
       (return content)))

(defn read-edn-file
  ; @param (string) filepath
  ;
  ; @return (*)
  [filepath]
  (let [file-content (read-file filepath)]
       (if (-> file-content string/trim some?)
           (-> file-content reader/string->mixed))))
           ; Az .edn fájl tartalma lehet string, map, vektor, stb. típus,
           ; ezért a read-edn-file függvény kimenetén nem lehetséges típusvizsgálatot végezni!

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
  ; @return (map)
  [filepath f & params]
  (let [edn    (read-edn-file    filepath)
        params (vector/cons-item params edn)
        output (apply          f params)]
       (write-edn-file! filepath output)
       (return output)))
