
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.api
    (:require [app-plugins.view-selector.engine :as engine]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: server-plugins.view-selector.api
;
; @usage
;  (ns my-namespace (:require [app-plugins.view-selector.api :as view-selector]))
;
;  (defn body [_ {:keys [view-id]}] ...)
;
;  (a/reg-event-fx :my-extension/render! [:ui/set-surface! {:view {:content    #'body
;                                                                  :subscriber [:view-selector/get-view-props :my-extension]}}]))
;
;  (a/reg-event-fx :my-extension/load! {:dispatch-n [[:ui/listen-to-process! :my-extension/synchronize!]
;                                                    [:ui/set-header-title!  "My extension"]
;                                                    [:ui/set-window-title!  "My extension"]
;                                                    [:my-extension/render!]]})
;
;  (a/dispatch [:sync/send-request! :my-extension/synchronize! {...}])
;
;  (a/dispatch [:router/go-to! "/my-extension"])
;  (a/dispatch [:router/go-to! "/my-extension/my-view"])
;
;  (a/dispatch [:view-selector/change-view! :my-extension :my-view])
;
;  (defn my-event [db _] (r view-selector/change-view! db :my-extension :my-view))
;  (a/reg-event-db :my-event my-event)



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.view-selector.engine
(def get-selected-view-id engine/get-selected-view-id)
(def get-view-props       engine/get-view-props)
(def change-view!         engine/change-view!)
