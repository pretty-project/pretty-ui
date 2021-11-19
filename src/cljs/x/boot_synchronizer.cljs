
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.28
; Description:
; Version: v0.3.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-synchronizer
    (:require [x.app-core.api :as a]
              [x.app-db.api   :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def TARGET-PATHS
     {:app-details     (db/path           :x.mid-core.config-handler/configs :app-details)
      :site-links      (db/path           :x.mid-core.config-handler/configs :site-links)
      :storage-details (db/path           :x.mid-core.config-handler/configs :storage-details)
      :reserved-routes (db/meta-item-path :x.app-router.route-handler/routes :reserved-server-routes)
      :user-account    (db/path           :x.app-user.account-handler/account)
      :user-profile    (db/path           :x.app-user.profile-handler/profile)
      :user-settings   (db/path           :x.app-user.settings-handler/settings)})



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.boot-synchronizer/synchronize-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-synchronizer/synchronize-app! #'app]
  (fn [_ [_ app]]
      [:x.app-sync/send-request! ::synchronize-app!
                                 {:method       :get
                                  :on-failure   [:x.app-core/->error-catched]
                                  :on-success   [:x.boot-loader/->app-synchronized app]
                                  :target-paths TARGET-PATHS
                                  :uri          "/synchronize-app"}]))
