
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.api
    (:require [x.server-environment.crawler-handler.lifecycles]
              [x.server-environment.crawler-handler.routes]
              [x.server-environment.crawler-handler.engine :as crawler-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-environment.crawler-handler.engine
(def crawler-rules crawler-handler.engine/crawler-rules)
