
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.15
; Description:
; Version: v0.3.6
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.system-routes
    (:require [mid-fruits.vector   :as vector]
              [server-fruits.http  :as http]
              [x.boot-synchronizer :as boot-synchronizer]
              [x.server-core.api   :as a]
              [x.server-db.api     :as db]
              [x.server-log.api    :as log]
              [x.server-media.api  :as media]
              [x.server-tools.pdf  :as pdf]
              [x.server-user.api   :as user]
              [x.server-views.api  :as views]))



;; -- Routes ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:my-route {:route-template "/my-route"
;              :get  {:handler my-get-handler}
;              :post {:handler my-post-handler}}}
(def SYSTEM-ROUTES
     {:synchronize-app
      {:route-template "/synchronize-app"
       :get {:handler boot-synchronizer/download-sync-data}}

      :media/query
      {:route-template "/media/query"
       :post #(http/map-wrap {:body (media/process-query! %)})}

      :media/upload-files
      {:route-template "/media/upload"
       :post #(http/map-wrap {:body (media/process-upload! %)})}

      :media/download-file
      {:route-template "/media/storage/:filename"
       :get {:handler media/download-file}}

      :media/download-thumbnail
      {:route-template "/media/thumbnails/:filename"
       :get {:handler media/download-thumbnail}}

      :pdf/generate-pdf
      {:route-template "/pdf/generate-pdf"
       :post {:handler pdf/generate-pdf!}}

      :pdf/temporary-file
      {:route-template "/pdf/:temporary-filename/:filename"
       :get {:handler pdf/download-generated-pdf}}

      :user/authenticate
      {:route-template "/user/authenticate"
       :post {:handler user/authenticate}}

      :user/logout
      {:route-template "/user/logout"
       :post {:handler user/logout}}})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:x.server-router/add-routes! SYSTEM-ROUTES]})
