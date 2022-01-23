
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.15
; Description:
; Version: v0.4.8
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.system-routes
    (:require [x.server-core.api        :as a]
              [x.server-environment.api :as environment]
              [x.server-media.api       :as media]
              [x.server-user.api        :as user]))



;; -- Routes ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:my-route {:route-template "/my-route"
;              :get  {:handler my-get-handler}
;              :post {:handler my-post-handler}}}
(def SYSTEM-ROUTES {:core/transfer-data
                    {:route-template "/synchronize-app"
                     :get {:handler a/download-transfer-data}}

                    :environment/robots.txt
                    {:route-template "/robots.txt"
                     :get {:handler environment/download-robots-txt}}

                    :media/download-file
                    {:route-template "/media/storage/:filename"
                     :get {:handler media/download-file}}

                    :media/download-thumbnail
                    {:route-template "/media/thumbnails/:filename"
                     :get {:handler media/download-thumbnail}}

                    :user/authenticate
                    {:route-template "/user/authenticate"
                     :post {:handler user/authenticate}}

                    :user/logout
                    {:route-template "/user/logout"
                     :post {:handler user/logout}}

                    :user/upload-user-settings-item
                    {:route-template "/user/upload-user-settings-item"
                     :post {:handler user/upload-user-settings-item!}}})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-init [:router/add-routes! SYSTEM-ROUTES]})
