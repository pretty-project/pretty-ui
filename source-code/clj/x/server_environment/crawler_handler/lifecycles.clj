
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.crawler-handler.lifecycles
    (:require [x.server-core.api                           :as x.core]
              [x.server-environment.crawler-handler.routes :as crawler-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:router/add-routes! {:environment/robots.txt  {:route-template "/robots.txt"
                                                                   :get {:handler crawler-handler.routes/download-robots-txt}}
                                         :environment/sitemap.xml {:route-template "/sitemap.xml"
                                                                   :get {:handler crawler-handler.routes/download-sitemap-xml}}}]})
