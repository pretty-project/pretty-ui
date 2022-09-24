
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.image-preloader.sample
    (:require [tools.image-preloader.api :as image-preloader]))



;; -- Az applikáció indításának várakoztatása egy kép sikeres letöltéséig -----
;; ----------------------------------------------------------------------------

(defn my-image
  []
  [image-preloader/component {:uri "/my-image.png"}])
