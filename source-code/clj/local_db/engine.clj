
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.engine
    (:require [mid-fruits.string :as string]
              [server-fruits.io  :as io]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (B)
(def MAX-FILESIZE (io/MB->B 10))

; @constant (string)
(def LOCAL-DB-PATH "monoset-environment/db/")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-name-valid?
  ; @param (*) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (string/nonempty? collection-name))

(defn collection-name->filepath
  ; @param (string) collection-name
  ;
  ; @return (string)
  [collection-name]
  (str LOCAL-DB-PATH collection-name ".edn"))
