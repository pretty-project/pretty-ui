
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.api
    (:require [x.server-db.api]
              [x.dictionary.api]
              [x.server-environment.api]
              [x.server-router.api]
              [x.views.api]
              [x.boot-loader.effects]
              [x.boot-loader.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.boot-loader.side-effects
(def start-server! side-effects/start-server!)
