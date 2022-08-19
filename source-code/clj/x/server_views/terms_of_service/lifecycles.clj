
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.terms-of-service.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:router/add-route! :terms-of-service/route
                                       {:client-event   [:views/render-terms-of-service!]
                                        :restricted?    true
                                        :route-template "/@app-home/terms-of-service"}]})
