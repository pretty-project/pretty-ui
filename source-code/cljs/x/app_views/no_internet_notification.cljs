
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.4.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.no-internet-notification
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-ui.api          :as ui]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (component)
  [bubble-id]
  [elements/polarity {:start-content [elements/label  {:content  :no-internet-connection}]
                      :end-content   [elements/button {:label    :refresh!
                                                       :on-click [:boot-loader/refresh-app!]
                                                       :preset   :primary-button}]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/blow-no-internet-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (and (r environment/browser-offline? db)
               (r ui/application-interface?    db))
          [:ui/blow-bubble! ::notification
                            {:body        {:content #'body}
                             :autopop?    false
                             :user-close? false}])))

(a/reg-lifecycles
  ::lifecycles
  {:on-browser-offline [:views/blow-no-internet-bubble?!]
   :on-app-launch      [:views/blow-no-internet-bubble?!]
   :on-browser-online  [:ui/pop-bubble! ::notification]})
