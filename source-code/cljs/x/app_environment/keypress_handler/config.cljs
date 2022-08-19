

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler.config
    (:require [x.app-core.api                           :as a]
              [x.app-environment.keypress-handler.state :as keypress-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (function)
(def KEYDOWN-LISTENER #(let [key-code (.-keyCode %)]
                            (if (get @keypress-handler.state/PREVENTED-KEYS key-code)
                                (.preventDefault %))
                            (a/dispatch [:environment/key-pressed key-code])))

; @constant (function)
(def KEYUP-LISTENER #(let [key-code (.-keyCode %)]
                          (if (get @keypress-handler.state/PREVENTED-KEYS key-code)
                              (.preventDefault %))
                          (a/dispatch [:environment/key-released key-code])))
