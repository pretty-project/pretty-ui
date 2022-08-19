
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.privacy-policy.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:router/add-route! :privacy-policy/route
                                       {:client-event   [:views/render-privacy-policy!]
                                        :route-template "/@app-home/privacy-policy"
                                        :restricted?    true}]})
