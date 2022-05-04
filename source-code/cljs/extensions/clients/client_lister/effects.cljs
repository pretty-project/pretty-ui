
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.effects
    (:require [extensions.clients.client-lister.views :as client-lister.views]
              [x.app-core.api                         :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-lister/load-lister!
  [:clients.client-lister/render-lister!])

(a/reg-event-fx
  :clients.client-lister/render-lister!
  [:ui/render-surface! :clients.client-lister/view
                       {:content #'client-lister.views/view}])
