
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.sente.api
    (:require [plugins.sente.websocket :as websocket]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; sente.websocket
(def ring-ajax-post                websocket/ring-ajax-post)
(def ring-ajax-get-or-ws-handshake websocket/ring-ajax-get-or-ws-handshake)
(def ch-chsk                       websocket/ch-chsk)
(def chsk-send!                    websocket/chsk-send!)
(def connected-uids                websocket/connected-uids)
