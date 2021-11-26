
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.4.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.no-internet-notification
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.api          :as ui]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/blow-no-internet-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (and (r environment/browser-offline? db)
               (r ui/application-interface?    db))
          [:ui/blow-bubble! ::notification
                            {:content     :no-internet-connection
                             :autopop?    false
                             :user-close? false
                             :primary-button
                             {:label    :refresh!
                              :on-click [:boot-loader/refresh-app!]
                              :preset   :primary-button}}])))

(a/reg-lifecycles
  ::lifecycles
  {:on-browser-offline [:views/blow-no-internet-bubble?!]
   :on-app-launch      [:views/blow-no-internet-bubble?!]
   :on-browser-online  [:ui/pop-bubble! ::notification]})
