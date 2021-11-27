
(ns x.server-developer.source-reader
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! :source-reader/route
                                    {:route-template "/docs"
                                     :client-event   [:developer/render-source-reader!]
                                     :restricted?    true}]})
