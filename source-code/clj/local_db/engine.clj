
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

(defn collection-id-valid?
  ; @param (*) collection-id
  ;
  ; @return (boolean)
  [collection-id]
  (string/nonempty? collection-id))

(defn collection-id->filepath
  ; @param (string) collection-id
  ;
  ; @return (string)
  [collection-id]
  (str LOCAL-DB-PATH collection-id ".edn"))
