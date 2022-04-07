
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.sample
    (:require [plugins.item-browser.api :as item-browser]
              [x.app-core.api           :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az item-browser plugin dokumentációjából hiányzó részeket az item-lister
; plugin dokumentációjában keresd!



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin beállításához mindenképpen szükséges a szerver-oldali
; [:item-browser/init-browser! ...] eseményt beállításához!



;; -- Az {:auto-title? true} beállítás használata -----------------------------
;; ----------------------------------------------------------------------------
