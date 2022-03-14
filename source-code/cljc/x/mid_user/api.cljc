
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.api
    (:require [x.mid-user.core.helpers           :as core.helpers]
              [x.mid-user.profile-handler.config :as profile-handler.config]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.core.helpers
(def user-roles->user-identified?   core.helpers/user-roles->user-identified?)
(def user-roles->user-unidentified? core.helpers/user-roles->user-unidentified?)

; x.mid-user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)
