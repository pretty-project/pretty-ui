
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler.helpers
    (:require [mid-fruits.form                      :as form]
              [mid-fruits.map                       :as map]
              [mid-fruits.string                    :as string]
              [server-fruits.hash                   :as hash]
              [x.server-user.core.helpers           :as core.helpers]
              [x.server-user.profile-handler.config :as profile-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-props->user-account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (namespaced map)
  [user-props]
  (let [user-account (select-keys user-props [:email-address :password :pin :roles])]
       (map/add-namespace user-account "user-account")))

(defn user-props-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) user-props
  ;  {:email-address (string)
  ;   :first-name (string)
  ;   :last-name (string)
  ;   :password (string)}
  ;
  ; @return (boolean)
  [{:keys [email-address first-name last-name password]}]
  (and (form/password?      password)
       (form/email-address? email-address)
       (string/length?      first-name 1 profile-handler.config/MAX-FIRST-NAME-LENGTH)
       (string/length?      last-name  1 profile-handler.config/MAX-FIRST-NAME-LENGTH)))

(defn user-props->user-account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) user-id
  ; @param (map) user-props
  ;
  ; @return (namespaced map)
  ;  {:user-account/email-address (string)
  ;   :user-account/id (string)
  ;   :user-account/password (string)
  ;   :user-account/permissions (map)
  ;   :user-account/pin (string)
  ;   :user-account/roles (strings vector)}
  [user-id {:keys [email-address password] :as user-props}]
  {:user-account/email-address email-address
   :user-account/id            user-id
   :user-account/permissions  {user-id "rw"}
   :user-account/roles        [user-id]
   :user-account/password     (hash/hmac-sha256 password email-address)
   :user-account/pin          (core.helpers/generate-pin)})

(defn user-props->user-profile
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) user-id
  ; @param (map) user-props
  ;
  ; @return (namespaced map)
  ;  {:user-profile/id (string)
  ;   :user-profile/permissions (map)}
  [user-id _]
  {:user-profile/id           user-id
   :user-profile/permissions {user-id "rw"}})

(defn user-props->user-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) user-id
  ; @param (map) user-props
  ;
  ; @return (namespaced map)
  ;  {:user-settings/id (string)
  ;   :user-settings/permissions (map)}
  [user-id _]
  {:user-settings/id           user-id
   :user-settings/permissions {user-id "rw"}})
