
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.api
    (:require [plugins.view-selector.body.events]
              [plugins.view-selector.body.subs]
              [plugins.view-selector.core.effects]
              [plugins.view-selector.routes.effects]
              [plugins.view-selector.routes.events]
              [plugins.view-selector.transfer.subs]
              [plugins.view-selector.body.views  :as body.views]
              [plugins.view-selector.core.events :as core.events]
              [plugins.view-selector.core.subs   :as core.subs]
              [plugins.view-selector.routes.subs :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.view-selector.body.views
(def body body.views/body)

; plugins.view-selector.core.events
(def change-view! core.events/change-view!)

; plugins.view-selector.core.subs
(def get-selected-view-id core.subs/get-selected-view-id)

; plugins.view-selector.routes.subs
(def get-view-route routes.subs/get-view-route)
