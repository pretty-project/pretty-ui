
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.23
; Description:
; Version: v0.6.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.themes
    (:require [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a :refer [r]]
              [x.app-user.api     :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-theme
  ; @return (string)
  [db _]
  (r user/get-user-settings-item db :selected-theme))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-selected-theme!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) theme-name
  ;
  ; @return (map)
  [db [_ theme-name]]
  (r user/set-user-settings-item! db :selected-theme theme-name))

(a/reg-event-db :x.app-ui/store-selected-theme! store-selected-theme!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/set-theme!
  ; @param (string) theme-name
  (fn [_ [_ theme-name]]
      {:dispatch-n [[:x.app-ui/store-selected-theme! theme-name]
                    [:x.app-ui/->theme-changed]]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/->theme-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [theme-name (r get-selected-theme db)]
           [:x.app-environment.element-handler/set-attribute!
            "x-body-container" "data-theme" theme-name])))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-login [:x.app-ui/->theme-changed]})
