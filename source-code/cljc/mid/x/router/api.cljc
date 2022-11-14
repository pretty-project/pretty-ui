
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.router.api
    (:require [mid.x.router.route-handler.subs :as route-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.router.route-handler.subs
(def get-app-home route-handler.subs/get-app-home)
(def use-app-home route-handler.subs/use-app-home)
