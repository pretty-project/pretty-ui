
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.docs.side-effects
    (:require [candy.api                    :refer [return]]
              [developer-tools.docs.config  :as docs.config]
              [developer-tools.docs.helpers :as docs.helpers]
              [io.api                       :as io]
              [mid-fruits.vector            :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-namespaces
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-path]
  (letfn [(f [result filepath]
             (let [extension (io/filepath->extension filepath)]
                  (if (vector/contains-item? docs.config/ALLOWED-EXTENSIONS extension)
                      (let [file-content (io/read-file filepath)]
                           (assoc-in result [filepath :docs] (docs.helpers/file-content->docs file-content)))
                      (return result))))]
         (let [file-list (io/all-file-list directory-path)]
              (reduce f {} file-list))))
