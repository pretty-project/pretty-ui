
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.config-handler.subs
    (:require [x.mid-core.config-handler.subs :as config-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.config-handler.subs
(def get-app-config      config-handler.subs/get-app-config)
(def get-app-config-item config-handler.subs/get-app-config-item)
