
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.23
; Description:
; Version: v0.6.8
; Compatibility: x4.4.6



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



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/set-theme!
  ; @param (string) theme-name
  ;
  ; @usage
  ;  [:x.app-ui/set-theme! "light"]
  (fn [{:keys [db]} [_ theme-name]]
      {:db       (r store-selected-theme! db theme-name)
       :dispatch [:x.app-ui/->theme-changed]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/->theme-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [theme-name (r get-selected-theme db)]
           [:environment/set-element-attribute! "x-body-container" "data-theme" theme-name])))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-login [:x.app-ui/->theme-changed]})
