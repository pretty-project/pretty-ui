
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.api
    (:require [app-plugins.view-selector.engine]
              [app-plugins.view-selector.effects]
              [app-plugins.view-selector.events :as events]
              [app-plugins.view-selector.subs   :as subs]
              [app-plugins.view-selector.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.view-selector.events
(def change-view! events/change-view!)

; app-plugins.view-selector.subs
(def get-selected-view-id subs/get-selected-view-id)

; app-plugins.view-selector.views
(def body   views/body)
(def header views/header)
(def view   views/view)
