
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.lifecycle-handler.sample)



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name on-server-init
;  Az on-server-boot előtt lefutó események.
;
; @name on-server-boot
;  A szerver indítása előtt lefutó események.
;
; @name on-server-launch
;  A szerver indítása után lefutó események.
;
; @name on-app-init
;  Az on-app-boot előtt lefutó események.
;
; @name on-app-boot
;  Az applikáció első renderelése előtt lefutó események.
;
; @name on-app-launch
;  Az applikáció első renderelése után lefutó események.
;
; @name on-login
;  (Nem vendég) bejelentkezéskor lefutó események.
;
; @name on-browser-online
;  Nomen est omen.
;
; @name on-browser-offline
;  Nomen est omen.
