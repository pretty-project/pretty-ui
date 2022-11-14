
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
;
; https://github.com/venantius/accountant
; By default, clicking a link to the currently active route will not trigger the navigation handler.
; You can disable this behavior and always trigger the navigation handler by setting reload-same-path?
; to true during configuration.
(def RELOAD-SAME-PATH? true)
