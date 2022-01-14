
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.28
; Description:
; Version: v0.3.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-synchronizer
    (:require [x.app-core.api :as a]
              [x.app-db.api   :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def TARGET-PATHS {:app-config    (db/path :core/app-config)
                   :site-config   (db/path :core/site-config)
                   :client-routes (db/path :router/client-routes)
                   :user-account  (db/path :user/account)
                   :user-profile  (db/path :user/profile)
                   :user-settings (db/path :user/settings)})



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :boot-synchronizer/synchronize-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-synchronizer/synchronize-app! #'app]
  (fn [cofx [_ app]]
      [:sync/send-request! :boot-synchronizer/synchronize-app!
                           {:method       :get
                            :on-failure   [:core/->error-catched {:cofx cofx :error "Failed to synchronize app!"}]
                            :on-success   [:boot-loader/->app-synchronized app]
                            :target-paths TARGET-PATHS
                            :uri          "/synchronize-app"}]))
