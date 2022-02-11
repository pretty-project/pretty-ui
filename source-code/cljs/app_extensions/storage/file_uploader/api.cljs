
(ns app-extensions.storage.file-uploader.api
    (:require [app-extensions.storage.file-uploader.dialogs]
              [app-extensions.storage.file-uploader.engine]
              [app-extensions.storage.file-uploader.events]
              [app-extensions.storage.file-uploader.queries]
              [app-extensions.storage.file-uploader.subs]
              [app-extensions.storage.file-uploader.views]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; - Egy időben több fájlfeltöltési folyamat is futtatható.
; - Egyszerre egy fájlfeltöltő nyitható meg, és minden fájlfeltöltő egyedi azonosítóval kell
;   rendelkezzen, mert az egyes fájlfeltöltők által indított feltöltési folyamatok időben
;   átfedésbe kerülhetnek egymással.
