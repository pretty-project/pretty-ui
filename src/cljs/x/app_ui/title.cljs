
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.09
; Description:
; Version: v0.6.2
; Compatibility: x4.2.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.title
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-window-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (string)
  [db [_ window-title]]
  (let [app-title (r a/get-app-detail db :app-title)]
       (let [window-title (r components/get-metamorphic-value db {:value window-title})]
            (str window-title " - " app-title))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/restore-default-title!
  (fn [{:keys [db]} _]
      (let [window-title (r a/get-app-detail db :app-title)]
           [:x.app-environment.window-handler/set-title! window-title])))

(a/reg-event-fx
  :x.app-ui/set-title!
  ; @param (metamorphic-value) window-title
  (fn [{:keys [db]} [_ window-title]]
      (let [window-title (r get-window-title db window-title)]
           [:x.app-environment.window-handler/set-title! window-title])))
