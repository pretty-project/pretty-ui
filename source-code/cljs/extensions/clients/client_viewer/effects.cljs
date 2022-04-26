
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.effects
    (:require [extensions.clients.client-viewer.views :as client-viewer.views]
              [x.app-core.api                         :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-viewer/load-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:clients.client-viewer/render-viewer!])

(a/reg-event-fx
  :clients.client-viewer/render-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :clients.client-viewer/view
                    {:view #'client-viewer.views/view}])
