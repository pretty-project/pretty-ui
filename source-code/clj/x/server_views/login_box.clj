
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.3.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.login-box
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! :login-box/route
                                    {:client-event   [:views/render-login-box!]
                                     :route-template "/login"}]})
