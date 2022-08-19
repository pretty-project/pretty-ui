

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.middleware-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (strings in vector)
; - A wrap-reload alapértelmezett beállítással használva az "src" mappa
;   fájljait figyeli
;
; - A monotech-hq/monoset könyvtárban a forráskód a "source-code" mappában van
;
; - A projektekben elhelyezett monotech-hq/monoset könyvtár a "monoset" mappában van
(def SOURCE-DIRECTORY-PATHS ["source-code" "monoset/source-code/clj" "monoset/source-code/cljc"])
