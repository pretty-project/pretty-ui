
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.effects
    (:require [extensions.clients.client-viewer.views :as client-viewer.views]
              [x.app-core.api                         :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-viewer/load-viewer!
  [:clients.client-viewer/render-viewer!])

(a/reg-event-fx
  :clients.client-viewer/render-viewer!
  [:ui/render-surface! :clients.client-viewer/view
                       {:content #'client-viewer.views/view}])
