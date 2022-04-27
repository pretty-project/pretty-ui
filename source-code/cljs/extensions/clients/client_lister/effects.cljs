
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.effects
    (:require [extensions.clients.client-lister.views :as client-lister.views]
              [x.app-core.api                         :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:clients.client-lister/render-lister!])

(a/reg-event-fx
  :clients.client-lister/render-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/render-surface! :clients.client-lister/view
                       {:view #'client-lister.views/view}])
