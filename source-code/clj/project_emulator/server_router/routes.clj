
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-router.routes
    (:require [pathom.api         :as pathom]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [project-emulator.server-ui.api :as ui]))



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
       :post #(http/map-wrap {:body (pathom/process-request! %)})}})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:router/add-routes! ROUTES]})
