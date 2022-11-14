
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.crawler-handler.lifecycles
    (:require [x.core.api                           :as x.core]
              [x.environment.crawler-handler.routes :as crawler-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:x.router/add-routes! {:x.environment/robots.txt  {:route-template "/robots.txt"
                                                                       :get {:handler crawler-handler.routes/download-robots-txt}}
                                           :x.environment/sitemap.xml {:route-template "/sitemap.xml"
                                                                       :get {:handler crawler-handler.routes/download-sitemap-xml}}}]})
