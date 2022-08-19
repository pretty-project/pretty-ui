

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.file-selector
    (:require [dom.file :as file]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-selector->files
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->files my-file-selector)
  ;
  ; @return (?)
  [file-selector]
  (-> file-selector .-files))

(defn file-selector->file-list
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->file-list my-file-selector)
  ;
  ; @return (?)
  [file-selector]
  (-> file-selector .-files array-seq))

(defn file-selector->file
  ; @param (DOM-element) file-selector
  ; @param (integer) file-dex
  ;
  ; @usage
  ;  (dom/file-selector->file my-file-selector 2)
  ;
  ; @return (file object)
  [file-selector file-dex]
  (-> file-selector .-files array-seq (nth file-dex)))

(defn file-selector->files-size
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->files-size my-file-selector)
  ;
  ; @return (B)
  [file-selector]
  (reduce #(+ %1 (.-size %2)) 0 (-> file-selector .-files array-seq)))

(defn file-selector->file-count
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->file-count my-file-selector)
  ;
  ; @return (integer)
  [file-selector]
  (-> file-selector .-files .-length))

(defn file-selector->any-file-selected?
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->any-file-selected? my-file-selector)
  ;
  ; @return (boolean)
  [file-selector]
  (-> file-selector .-files .-length (> 0)))

(defn file-selector->mime-types
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->mime-types my-file-selector)
  ;
  ; @return (strings in vector)
  [file-selector]
  (reduce #(conj %1 (.-type %2)) [] (-> file-selector .-files array-seq)))

(defn file-selector->files-data
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->files-data my-file-selector)
  ;
  ; @return (maps in vector)
  [file-selector]
  (reduce-kv #(conj %1 (file/file->file-data %2 %3)) [] (-> file-selector .-files array-seq vec)))

(defn file-selector->files-meta
  ; @param (DOM-element) file-selector
  ;
  ; @usage
  ;  (dom/file-selector->files-meta my-file-selector)
  ;
  ; @return (map)
  ;  {:file-count (integer)
  ;   :files-size (B)}
  [file-selector]
  {:file-count (file-selector->file-count file-selector)
   :files-size (file-selector->files-size file-selector)})

(defn file-selector->image-data-url
  ; @param (DOM-element) file-selector
  ; @param (integer) file-dex
  ;
  ; @usage
  ;  (dom/file-selector->image-data-url my-file-selector 2)
  ;
  ; @return (string)
  [file-selector file-dex]
  (let [file        (file-selector->file file-selector file-dex)
        file-reader (js/FileReader.)]))
