
(ns x.app-developer.source-reader
    (:require [x.app-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/render-source-reader!
  [:ui/set-surface! ::view {:content #'view}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! ::route
                                    {:route-template "/docs"
                                     :route-event    [:developer/render-source-reader!]}]})
