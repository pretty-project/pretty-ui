
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.09
; Description:
; Version: v0.6.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.title
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-window-title-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-value) window-title
  ;
  ; @return (string)
  [db [_ window-title]]
  (if-let [window-title (r components/get-metamorphic-value db {:value window-title})]
          (let [app-title (r a/get-app-detail db :app-title)]
               (str window-title " - " app-title))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/restore-default-window-title!
  (fn [{:keys [db]} _]
      (let [window-title (r a/get-app-detail db :app-title)]
           [:environment/set-window-title! window-title])))

(a/reg-event-fx
  :ui/set-window-title!
  ; @param (metamorphic-value) window-title
  (fn [{:keys [db]} [_ window-title]]
      (if-let [window-title (r get-window-title-value db window-title)]
              [:environment/set-window-title! window-title]
              [:ui/restore-default-window-title!])))