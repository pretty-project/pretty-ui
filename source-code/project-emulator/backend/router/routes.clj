
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns backend.router.routes
    (:require [backend.ui.api     :as ui]
              [pathom.api         :as pathom]
              [sente.api          :as sente]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def ROUTES
     {:main
      {:route-template "/"
       :get #(http/html-wrap {:body (ui/main %)})
       :js "app.js"}

;     :site
;     {:route-template "/"
;      :get #(http/html-wrap {:body (ui/main %)})
;      :js "site.js"}
;
;     :admin
;     {:route-template "/admin"
;      :get #(http/html-wrap {:body (ui/main %)})
;      :js "admin.js"}

      :db/query
      {:route-template "/query"
       :post #(http/map-wrap {:body (pathom/process-request! %)})}

      ; WebSocket
      :wss/channel
      {:route-template "/chsk"
       :get  #(sente/ring-ajax-get-or-ws-handshake %)
       :post #(sente/ring-ajax-post                %)}})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-init [:router/add-routes! ROUTES]})
