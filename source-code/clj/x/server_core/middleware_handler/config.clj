
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.middleware-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (strings in vector)
;  A wrap-reload alapértelmezett beállítással használva az "src" mappa fájljait figyeli
(def SOURCE-DIRECTORY-PATHS [; A monotech-hq/monoset könyvtárban a forráskód a "source-code" mappában van
                             "source-code" "deps"

                             ; A projektekben elhelyezett monotech-hq/monoset könyvtár a "monoset" mappában van
                             "monoset/source-code" "monoset/deps"])
