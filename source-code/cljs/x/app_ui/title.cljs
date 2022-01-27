
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.09
; Description:
; Version: v0.8.0
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.title
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-ui.header      :as header]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-window-title-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-content) window-title
  ;
  ; @return (string)
  [db [_ window-title]]
  (if-let [window-title (r components/get-metamorphic-value db {:value window-title})]
          (let [app-title (r a/get-app-config-item db :app-title)]
               (str window-title " - " app-title))
          (r a/get-app-config-item db :app-title)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/restore-default-window-title!
  ; @usage
  ;  [:ui/restore-default-window-title!]
  (fn [{:keys [db]} _]
      (let [window-title (r a/get-app-config-item db :app-title)]
           {:environment/set-window-title! window-title})))

(a/reg-event-fx
  :ui/set-window-title!
  ; @param (metamorphic-content) window-title
  ;
  ; @usage
  ;  [:ui/set-window-title! "My title"]
  (fn [{:keys [db]} [_ window-title]]
      (let [window-title (r get-window-title-value db window-title)]
           {:environment/set-window-title! window-title})))

(a/reg-event-fx
  :ui/set-title!
  ; @param (metamorphic-content) title
  ;
  ; @usage
  ;  [:ui/set-title! "My title"]
  (fn [{:keys [db]} [_ title]]
      (let [window-title (r get-window-title-value db title)]
           {:db (r header/set-header-title! db title)
            :environment/set-window-title! window-title})))
