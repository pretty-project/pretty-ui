
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.api
    (:require [x.server-environment.crawler-handler.lifecycles]
              [x.server-environment.crawler-handler.helpers :as crawler-handler.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-environment.crawler-handler.helpers
(def crawler-rules crawler-handler.helpers/crawler-rules)
