
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.2
; Compatibility: x4.4.6



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
;  (a/dispatch [:sync/send-request! :my-namespace/synchronize! {...}])
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
