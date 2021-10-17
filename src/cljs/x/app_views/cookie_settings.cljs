
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.05
; Description:
; Version: v0.2.6
; Compatibility: x4.2.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.cookie-settings
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-views.settings  :as settings]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  WARNING! XXX#0459



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cookie-settings-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/button {:preset   :cancel-button
                    :on-click [:x.app-ui/close-popup! popup-id]}])

(defn- cookie-settings-save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/button {:preset  :save-button
                    :variant :transparent
                    :on-click {:dispatch-n [[:x.app-ui/close-popup! popup-id]
                                            [::accept-cookie-settings!]]}}])

(defn- cookie-settings-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [_]
  [elements/label {:content :privacy-settings}])

(defn- cookie-settings-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/polarity {:start-content  [:<> [cookie-settings-cancel-button popup-id]]
                      :middle-content [:<> [elements/separator {:size :xs :orientation :vertical}]
                                           [cookie-settings-label popup-id]
                                           [elements/separator {:size :xs :orientation :vertical}]]
                      :end-content    [:<> [cookie-settings-save-button popup-id]]}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::accept-cookie-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-environment.cookie-handler/->settings-changed])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup!
   ::view
   {:content          #'settings/cookie-settings
    :horizontal-align :left
    :label-bar        {:content #'cookie-settings-label-bar}
    :layout           :boxed
    :user-close?      false}])
