
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.0
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
;  (defn get-body-props [db _] (r view-selector/get-body-props db))
;  (a/reg-sub ::get-body-props get-body-props)
;
;  (defn get-header-props [db _] (r view-selector/get-header-props db))
;  (a/reg-sub ::get-header-props get-header-props)
;
;  (defn body [_ {:keys [selected-view]}] ...)
;
;  (a/reg-event-fx :my-extension/render! [:ui/set-surface! {:content    #'body
;                                                           :subscriber [::get-body-props]}]))
;
;  (a/dispatch [:sync/send-request! :my-namespace/synchronize! {...}])
;
;  (a/dispatch [:router/go-to! "/my-extension"])
;  (a/dispatch [:router/go-to! "/my-extension/my-view"])
;
;  (a/dispatch [:view-selector/change-view! :my-extension :my-view])



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.view-selector.engine
(def request-id        engine/request-id)
(def get-selected-view engine/get-selected-view)
(def get-header-props  engine/get-header-props)
(def get-body-props    engine/get-body-props)
(def change-view!      engine/change-view!)
