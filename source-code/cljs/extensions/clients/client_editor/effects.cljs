
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.effects
    (:require [extensions.clients.client-editor.views :as client-editor.views]
              [x.app-core.api                         :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-editor/load-editor!
  [:clients.client-editor/render-editor!])

(a/reg-event-fx
  :clients.client-editor/render-editor!
  [:ui/render-surface! :clients.client-editor/view
                       {:content #'client-editor.views/view}])
