
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.keypress-handler.config
    (:require [re-frame.api                             :as r]
              [x.environment.keypress-handler.state :as keypress-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (function)
(def KEYDOWN-LISTENER #(let [key-code (.-keyCode %)]
                            (if (get @keypress-handler.state/PREVENTED-KEYS key-code)
                                (.preventDefault %))
                            (r/dispatch [:x.environment/key-pressed key-code])))

; @constant (function)
(def KEYUP-LISTENER #(let [key-code (.-keyCode %)]
                          (if (get @keypress-handler.state/PREVENTED-KEYS key-code)
                              (.preventDefault %))
                          (r/dispatch [:x.environment/key-released key-code])))
