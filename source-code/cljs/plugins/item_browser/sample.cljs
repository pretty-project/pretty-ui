
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.sample
    (:require [plugins.item-browser.api :as item-browser]
              [re-frame.api             :as r :refer [r]]))



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

; ...
; A body komponens {:label-key ...} paraméterét is szükséges megadni
; az {:auto-title? true} beállítás használatához!



;; -- Az {:default-item-id "..."} paraméter használata ------------------------
;; ----------------------------------------------------------------------------

; ...
