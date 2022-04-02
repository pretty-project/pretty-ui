
(ns backend.router.routes
    (:require [backend.ui.api     :as ui]
              [sente.api          :as sente]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def ROUTES {:ui/main
             {:route-template "/"
              :get #(http/html-wrap {:body (ui/main %)})}

;            :site
;            {:route-template "/"
;             :get #(http/html-wrap {:body (ui/main %)})
;             :core-js "site.js"}

             ; WebSocket
             :wss/channel
             {:route-template "/chsk"
              :get  #(sente/ring-ajax-get-or-ws-handshake %)
              :post #(sente/ring-ajax-post                %)}})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-routes! ROUTES]})
