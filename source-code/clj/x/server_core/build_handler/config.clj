
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.build-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;  - A "resources/public" mappában elhelyezett "x.app-build.edn" fájl minden
;    esetben a lefordított JAR fájl része!
;  - A ".gitignore" fájlban is aktualizálni kell a fájl elérési útvonalát,
;    annak esetleges megváltoztatásakor!
(def APP-BUILD-FILEPATH "resources/public/x.app-build.edn")

; @constant (string)
(def INITIAL-APP-BUILD "0.0.1")
