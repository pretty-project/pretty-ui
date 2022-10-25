
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.sente.websocket
    (:require [taoensso.sente :as sente]
              [ring.middleware.anti-forgery            :refer [wrap-anti-forgery]]

              [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]))
             ;[taoensso.sente.server-adapters.http-kit :refer [get-sch-adapter]



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(let [{:keys [ch-recv send-fn connected-uids ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {})]

     (def ring-ajax-post                ajax-post-fn)
     (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
     (def ch-chsk                       ch-recv)         ; ChannelSocket's receive channel
     (def chsk-send!                    send-fn)         ; ChannelSocket's send API fn
     (def connected-uids                connected-uids)) ; Watchable, read-only atom
