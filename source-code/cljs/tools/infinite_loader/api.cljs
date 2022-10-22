
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.infinite-loader.api
    (:require [tools.infinite-loader.effects]
              [tools.infinite-loader.events]
              [tools.infinite-loader.subs]
              [tools.infinite-loader.views :as views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; tools.infinite-loader.views
(def component views/component)
