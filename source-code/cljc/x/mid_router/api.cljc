
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.api
    (:require [x.mid-router.route-handler.subs :as route-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler.subs
(def get-app-home route-handler.subs/get-app-home)
(def use-app-home route-handler.subs/use-app-home)
