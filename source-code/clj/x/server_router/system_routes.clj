
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.15
; Description:
; Version: v0.4.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.system-routes
    (:require [x.boot-synchronizer :as boot-synchronizer]
              [x.server-core.api   :as a]
              [x.server-media.api  :as media]
              [x.server-user.api   :as user]))



;; -- Routes ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:my-route {:route-template "/my-route"
;              :get  {:handler my-get-handler}
;              :post {:handler my-post-handler}}}
(def SYSTEM-ROUTES {:synchronize-app
                    {:route-template "/synchronize-app"
                     :get {:handler boot-synchronizer/download-sync-data}}

                    ; WARNING! DEPRECATED! DO NOT USE!
                    ;  Ha majd a media extension kezeli a szerver oldali fájlkezelést,
                    ;  akkor (mivel extension) nem rendszer-szintű lesz a hozzá tartozó útvonal
                    :media/download-file
                    {:route-template "/media/storage/:filename"
                     :get {:handler media/download-file}}
                    ; WARNING! DEPRECATED! DO NOT USE!

                    ; WARNING! DEPRECATED! DO NOT USE!
                    :media/download-thumbnail
                    {:route-template "/media/thumbnails/:filename"
                     :get {:handler media/download-thumbnail}}
                    ; WARNING! DEPRECATED! DO NOT USE!

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
