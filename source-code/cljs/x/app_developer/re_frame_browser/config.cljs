

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def SYSTEM-KEYS [:core :developer :dictionary :elements :environment :gestures :locales :plugins
                  :router :sync :tools :user :ui

                  ; TEMP
                  :components/primary :core/build-handler :elements/primary :environment/keypress-events
                  :environment/viewport-data :ui/bubbles :ui/popups :ui/surface :elements/options :elements/values])
