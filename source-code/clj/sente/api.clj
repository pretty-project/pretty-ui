
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
