
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.3.6
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.no-internet-notification
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.api          :as ui]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::blow-bubble!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/blow-bubble!
   ::notification
   {:content     :no-internet-connection
    :autopop?    false
    :user-close? false
    :primary-button
    {:label    :refresh!
     :on-click [:x.boot-loader/refresh-app!]
     :preset   :action-button}}])

(a/reg-event-fx
  ::blow-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (and (r environment/browser-offline? db)
               (r ui/application-interface?    db))
          [::blow-bubble!])))

(a/reg-lifecycles
  ::lifecycles
  {:on-browser-offline [::blow-bubble?!]
   :on-app-launch      [::blow-bubble?!]
   :on-browser-online  [:x.app-ui/pop-bubble! ::notification]})
