
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.api
    (:require [plugins.view-selector.core.effects]
              [plugins.view-selector.mount.events]
              [plugins.view-selector.mount.subs]
              [plugins.view-selector.transfer.subs]
              [plugins.view-selector.core.events :as core.events]
              [plugins.view-selector.core.subs   :as core.subs]
              [plugins.view-selector.core.views  :as core.views]
              [plugins.view-selector.routes.subs :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.view-selector.core.events
(def change-view! core.events/change-view!)

; plugins.view-selector.core.subs
(def get-selected-view-id core.subs/get-selected-view-id)

; plugins.view-selector.core.views
(def body core.views/body)

; plugins.view-selector.routes.subs
(def get-view-route routes.subs/get-view-route)
