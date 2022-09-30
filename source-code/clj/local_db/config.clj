
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.config
    (:require [server-fruits.io  :as io]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (B)
(def MAX-FILESIZE (io/MB->B 10))

; @constant (string)
(def LOCAL-DB-PATH "environment/db/")
