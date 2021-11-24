
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!

;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-router.routes
    (:require [project-emulator.server-views.api :as views]
              [pathom.api         :as pathom]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def ROUTES
     {:main
      {:route-template "/"
       :get #(http/html-wrap {:body (views/main %)})
       :js "app.js"}

      :admin
      {:route-template "/admin"
       :get #(http/html-wrap {:body (views/main %)})
       :js "app.js"}

      :db/query
      {:route-template "/query"
       :post #(http/map-wrap {:body (pathom/process-request! %)})}})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:router/add-routes! ROUTES]})
