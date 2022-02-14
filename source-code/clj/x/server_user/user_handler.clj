
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.4.0
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.form    :as form]
              [mid-fruits.string  :as string]
              [mongo-db.api       :as mongo-db]
              [server-fruits.hash :as hash]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [x.server-user.account-handler :as account-handler]
              [x.server-user.engine          :as engine]
              [x.server-user.profile-handler :as profile-handler]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-props->user-account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (namespaced map)
  [user-props]
  (let [user-account (select-keys user-props [:email-address :password :pin :roles])]
       (db/document->namespaced-document user-account "user-account")))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- prototype-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespace) keyword
  ;
  ; @return (function)
  [namespace]
  #(mongo-db/added-document-prototype {:session account-handler/SYSTEM-ACCOUNT} namespace %))

(defn- user-props-valid?
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
       (string/length?      first-name 1 profile-handler/MAX-FIRST-NAME-LENGTH)
       (string/length?      last-name  1 profile-handler/MAX-FIRST-NAME-LENGTH)))

(defn- user-props->user-account
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
   :user-account/pin          (engine/generate-pin)})

(defn- user-props->user-profile
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

(defn- user-props->user-settings
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



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-user!
  ; @param (map) user-props
  ;  {:email-address (string)
  ;   :first-name (string)
  ;   :password (string)
  ;   :last-name (string)}
  ;
  ; @usage
  ;  (user/add-user! {...})
  ;
  ; @return (boolean)
  [{:keys [email-address password] :as user-props}]
  (if (user-props-valid? user-props)
      (if-not (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address email-address})
              (let [user-id       (mongo-db/generate-id)
                    user-account  (user-props->user-account  user-id user-props)
                    user-profile  (user-props->user-profile  user-id user-props)
                    user-settings (user-props->user-settings user-id user-props)]
                   (boolean (and (mongo-db/insert-document! "user_accounts" user-account  {:prototype-f (prototype-f :user-account)})
                                 (mongo-db/insert-document! "user_profiles" user-profile  {:prototype-f (prototype-f :user-profile)})
                                 (mongo-db/insert-document! "user_settings" user-settings {:prototype-f (prototype-f :user-settings)})))))))

; @usage
;  {:user/add-user! {...}}
(a/reg-fx :user/add-user! add-user!)
