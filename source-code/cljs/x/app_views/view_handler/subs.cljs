
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.view-handler.subs
    (:require [x.mid-views.view-handler.subs :as view-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-views.view-handler.subs
(def get-error-screen view-handler.subs/get-error-screen)
(def get-login-screen view-handler.subs/get-login-screen)
(def get-menu-screen  view-handler.subs/get-menu-screen)