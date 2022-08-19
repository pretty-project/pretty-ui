

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler.config
    (:require [x.mid-user.profile-handler.config :as profile-handler.config]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)
(def MAX-FIRST-NAME-LENGTH       profile-handler.config/MAX-FIRST-NAME-LENGTH)
(def MAX-LAST-NAME-LENGTH        profile-handler.config/MAX-LAST-NAME-LENGTH)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
;  {:user-profile/birthday (string)
;   :user-profile/first-name (string)
;   :user-profile/last-name (string)}
(def ANONYMOUS-USER-PROFILE {:user-profile/birthday   "1969-04-20"
                             :user-profile/first-name "Guest"
                             :user-profile/last-name  "User"})
