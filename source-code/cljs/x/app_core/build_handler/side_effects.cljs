
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.build-handler.side-effects
    (:require [x.mid-core.build-handler.side-effects :as build-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.build-handler.side-effects
(def app-build build-handler.side-effects/app-build)
