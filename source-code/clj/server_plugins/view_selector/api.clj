
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.api
    (:require [server-plugins.view-selector.engine :as engine]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: app-plugins.view-selector.api
;
; @usage
;  (ns my-namespace (:require [server-plugins.view-selector.api :as view-selector]))
;
; @usage
;  (a/dispatch [:view-selector/initialize! :my-extension])
;
; @usage
;  (a/dispatch [:view-selector/initialize! :my-extension {:default-view-id :my-view}])
;
; @usage
;  (a/dispatch [:view-selector/initialize! :my-extension {:default-view-id  :my-view
;                                                         :allowed-view-ids [:my-view :your-view :our-view]}])
