
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler.helpers
    (:require [forms.api                              :as forms]
              [mid-fruits.map                         :as map]
              [mid-fruits.string                      :as string]
              [mid-fruits.vector                      :as vector]
              [re-frame.api                           :as r]
              [server-fruits.hash                     :as hash]
              [x.server-user.core.helpers             :as core.helpers]
              [x.server-user.profile-handler.config   :as profile-handler.config]
              [x.server-user.settings-handler.helpers :as settings-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (and (forms/password?      password)
       (forms/email-address? email-address)
       (or (string/length? first-name 1 profile-handler.config/MAX-FIRST-NAME-LENGTH)
           (string/length? last-name  1 profile-handler.config/MAX-LAST-NAME-LENGTH))))

(defn user-props->user-account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) user-id
  ; @param (map) user-props
  ;  {}
  ;
  ; @return (namespaced map)
  ;  {:user-account/email-address (string)
  ;   :user-account/id (string)
  ;   :user-account/password (string)
  ;   :user-account/permissions (map)
  ;   :user-account/pin (string)
  ;   :user-account/roles (strings vector)}
  [user-id {:keys [email-address password pin roles] :as user-props}]
  (let [pin (str (or pin (core.helpers/generate-pin)))]
       {:user-account/email-address email-address
        :user-account/id            user-id
        :user-account/permissions  {user-id "rw"}
        :user-account/roles        (vector/cons-item roles user-id)
        :user-account/password     (hash/hmac-sha256 password email-address)
        :user-account/pin          (hash/hmac-sha256 pin      email-address)}))

(defn user-props->user-profile
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) user-id
  ; @param (map) user-props
  ;  {:first-name (string)(opt)
  ;   :last-name (string)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:user-profile/first-name (string)
  ;   :user-profile/id (string)
  ;   :user-profile/last-name (string)
  ;   :user-profile/locale (keyword)
  ;   :user-profile/permissions (map)}
  [user-id {:keys [first-name last-name]}]
  (let [app-locale @(r/subscribe [:core/get-app-config-item :app-locale])]
       {:user-profile/first-name   first-name
        :user-profile/id           user-id
        :user-profile/last-name    last-name
        :user-profile/locale       app-locale
        :user-profile/permissions {user-id "rw"}}))

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
  (merge @(r/subscribe [:user/get-default-user-settings])
          {:user-settings/id           user-id
           :user-settings/permissions {user-id "rw"}}))
