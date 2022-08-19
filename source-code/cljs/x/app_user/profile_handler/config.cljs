
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.profile-handler.config
    (:require [x.mid-user.profile-handler.config :as profile-handler.config]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)
(def MAX-FIRST-NAME-LENGTH       profile-handler.config/MAX-FIRST-NAME-LENGTH)
(def MAX-LAST-NAME-LENGTH        profile-handler.config/MAX-LAST-NAME-LENGTH)
