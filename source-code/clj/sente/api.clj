

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns sente.api
    (:require [sente.websocket :as websocket]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; sente.websocket
(def ring-ajax-post                websocket/ring-ajax-post)
(def ring-ajax-get-or-ws-handshake websocket/ring-ajax-get-or-ws-handshake)
(def ch-chsk                       websocket/ch-chsk)
(def chsk-send!                    websocket/chsk-send!)
(def connected-uids                websocket/connected-uids)
