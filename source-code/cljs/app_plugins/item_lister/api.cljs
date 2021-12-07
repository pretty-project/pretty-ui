
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.api
    (:require [app-plugins.item-lister.dialogs]
              [app-plugins.item-lister.engine]
              [app-plugins.item-lister.views :as views]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: server-plugins.item-lister.api
;
; @usage
;  (ns my-namespace (:require [app-plugins.item-lister.api :item-lister]))
;
;  (defn list-item [item-dex item] ...)
;  (defn view      [_] [:<> [item-lister/header :my-extension :my-type]
;                           [item-lister/body   :my-extension :my-type {:list-item #'list-item}]])
;  (a/reg-event-fx :my-extension/render-my-type-lister! [:ui/set-surface! {:view {:content #'view}}])
;
;  (a/dispatch [:router/go-to! "/my-extension"])



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-lister.views
(def header views/header)
(def body   views/body)
