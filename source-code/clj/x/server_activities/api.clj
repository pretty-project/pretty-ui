
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-activities.api
    (:require [x.server-activities.channel-handler.side-effects :as channel-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-activities.channel-handler.side-effects
(def notifiy-client! channel-handler.side-effects/notifiy-client!)
