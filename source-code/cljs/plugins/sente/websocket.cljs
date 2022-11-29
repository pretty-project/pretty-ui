
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.sente.websocket
    (:require-macros [cljs.core.async.macros :as asyncm :refer (go go-loop)])
    (:require        [cljs.core.async        :as async  :refer (<! >! put! chan)]
                     [taoensso.sente         :as sente  :refer (cb-success?)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def ?csrf-token (when-let [el (.getElementById js/document "sente-csrf-token")]
                           (.getAttribute el "data-csrf-token")))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client! "/chsk" ?csrf-token {:type :auto})] ; e/o #{:auto :ajax :ws}

     (def chsk       chsk)
     (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
     (def chsk-send! send-fn) ; ChannelSocket's send API fn
     (def chsk-state state))  ; Watchable, read-only atom
