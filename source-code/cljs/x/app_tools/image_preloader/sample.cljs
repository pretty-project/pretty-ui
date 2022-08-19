
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-preloader.sample
    (:require [x.app-tools.api :as tools]))



;; -- Az applikáció indításának várakoztatása egy kép sikeres letöltéséig -----
;; ----------------------------------------------------------------------------

(defn my-image
  []
  [tools/image-preloader {:uri "/my-image.png"}])
