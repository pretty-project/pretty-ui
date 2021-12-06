
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
  ; @return (keyword)
  [db _]
  (r user/get-user-settings-item db :selected-theme))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-selected-theme!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) theme-id
  ;
  ; @return (map)
  [db [_ theme-id]]
  (r user/set-user-settings-item! db :selected-theme theme-id))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/change-theme!
  ; @param (keyword) theme-id
  ;
  ; @usage
  ;  [:ui/change-theme! :light]
  (fn [{:keys [db]} [_ theme-id]]
      {:db       (r store-selected-theme! db theme-id)
       :dispatch [:ui/->theme-changed]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/->theme-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [theme-id (r get-selected-theme db)]
           [:environment/set-element-attribute! "x-body-container" "data-theme" (name theme-id)])))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-login [:ui/->theme-changed]})
