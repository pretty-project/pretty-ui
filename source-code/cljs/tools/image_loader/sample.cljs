
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.image-loader.sample
    (:require [tools.image-loader.api :as image-loader]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-image
  []
  [image-loader/component {:uri "/my-image.png"}])
