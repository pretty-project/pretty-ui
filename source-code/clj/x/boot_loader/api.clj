
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.api
    (:require [x.server-developer.api]
              [x.server-dictionary.api]
              [x.server-environment.api]
              [x.server-router.api]
              [x.server-views.api]
              [x.boot-loader.side-effects :as side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.boot-loader.side-effects
(def start-server! side-effects/start-server!)
