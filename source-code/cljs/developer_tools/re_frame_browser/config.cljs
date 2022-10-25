
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
(def SYSTEM-KEYS [:core :developer-tools :dictionary :elements :engines :environment
                  :gestures :locales :plugins :router :sync :tools :user :ui :views

                  ; TEMP
                  :components/primary :core/build-handler :elements/primary :environment/keypress-events
                  :environment/viewport-data :elements/options :elements/values])
