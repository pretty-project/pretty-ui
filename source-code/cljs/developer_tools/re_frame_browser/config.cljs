
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.re-frame-browser.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def SYSTEM-KEYS [:developer-tools :elements :engines :plugins
                  :x.components :x.core :x.dictionary :x.environment :x.gestures :x.locales
                  :x.router :x.sync :tools :x.user :x.ui :x.views

                  ; TEMP
                  :x.core/build-handler :elements/primary :x.environment/keypress-events])
