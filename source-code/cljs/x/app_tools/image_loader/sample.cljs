
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-loader.sample
    (:require [x.app-tools.api :as tools]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-image
  []
  [tools/image-loader {:uri "/my-image.png"}])
