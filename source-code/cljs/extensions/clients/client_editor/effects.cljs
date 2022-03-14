
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.effects
    (:require [extensions.clients.client-editor.views :as client-editor.views]
              [x.app-core.api                         :as a]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-editor/load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:clients.client-editor/render-editor!])

(a/reg-event-fx
  :clients.client-editor/render-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :clients.client-editor/view
                    {:view #'client-editor.views/view}])