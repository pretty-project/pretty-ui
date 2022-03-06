
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.crawler-handler.lifecycles
    (:require [x.server-core.api                           :as a]
              [x.server-environment.crawler-handler.routes :as crawler-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-routes! {:environment/robots.txt  {:route-template "/robots.txt"
                                                                   :get {:handler crawler-handler.routes/download-robots-txt}}
                                         :environment/sitemap.xml {:route-template "/sitemap.xml"
                                                                   :get {:handler crawler-handler.routes/download-sitemap-xml}}}]})
