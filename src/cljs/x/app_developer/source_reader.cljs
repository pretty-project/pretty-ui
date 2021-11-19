
(ns x.app-developer.source-reader
    (:require [x.app-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  [])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  [:x.app-ui/set-surface! ::view {:content #'view}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route! ::route
                                          {:route-template "/docs"
                                           :route-event    [::render!]}]})
