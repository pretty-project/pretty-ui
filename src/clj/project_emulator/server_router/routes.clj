
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-router.routes
    (:require [server-fruits.http            :as http]
              [project-emulator.server-db.api    :as db]
              [project-emulator.server-views.api :as views]
              [x.server-core.api             :as a]))



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
       :get #(http/html-wrap {:body (views/admin %)})
       :js "app.js"}

      :db/query
      {:route-template "/db/query"
       :post #(http/map-wrap {:body (db/process! %)})}})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::on-app-init-events
  [:x.server-router/add-routes! ROUTES])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [::on-app-init-events]})
