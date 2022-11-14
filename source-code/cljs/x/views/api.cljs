
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.views.api
    (:require [x.views.view-handler.effects]
              [x.views.view-handler.subs :as view-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.views.view-handler.subs
(def get-error-screen view-handler.subs/get-error-screen)
(def get-login-screen view-handler.subs/get-login-screen)
