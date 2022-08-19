

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.file
    (:require [mid-fruits.io :as io]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file->filesize
  ; @param (file object) file
  ;
  ; @usage
  ;  (dom/file->filesize my-file)
  ;
  ; @return (B)
  [file]
  (.-size file))

(defn file->filename
  ; @param (file object) file
  ;
  ; @usage
  ;  (dom/file->filename my-file)
  ;
  ; @return (string)
  [file]
  (.-name file))

(defn file->mime-type
  ; @param (file object) file
  ;
  ; @usage
  ;  (dom/file->mime-type my-file)
  ;
  ; @return (string)
  [file]
  (.-type file))

(defn file->image?
  ; @param (file object) file
  ;
  ; @usage
  ;  (dom/file->image? my-file)
  ;
  ; @return (boolean)
  [file]
  (-> file .-type io/mime-type->image?))

(defn file->file-data
  ; @param (integer)(opt) file-dex
  ; @param (file object) file
  ;
  ; @usage
  ;  (dom/file->file-data 2 my-file)
  ;
  ; @return (map)
  ;  {:file-dex (integer)
  ;   :filename (string)
  ;   :filesize (B)
  ;   :mime-type (string)
  ;   :object-url (string)}
  ([file]
   (file->file-data nil file))

  ([file-dex file]
   {:file-dex   file-dex
    :filename   (.-name file)
    :filesize   (.-size file)
    :mime-type  (.-type file)
    :object-url (.createObjectURL js/URL file)}))
