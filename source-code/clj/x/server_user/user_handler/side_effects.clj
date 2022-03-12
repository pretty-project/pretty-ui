
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler.side-effects
    (:require [mongo-db.api                         :as mongo-db]
              [x.server-core.api                    :as a]
              [x.server-user.account-handler.config :as account-handler.config]
              [x.server-user.user-handler.engine    :as user-handler.engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn prototype-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespace) keyword
  ;
  ; @return (function)
  [namespace]
  #(mongo-db/added-document-prototype {:session account-handler.config/SYSTEM-ACCOUNT} namespace %))



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
  (if (user-handler.engine/user-props-valid? user-props)
      (if-not (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address email-address})
              (let [user-id       (mongo-db/generate-id)
                    user-account  (user-handler.engine/user-props->user-account  user-id user-props)
                    user-profile  (user-handler.engine/user-props->user-profile  user-id user-props)
                    user-settings (user-handler.engine/user-props->user-settings user-id user-props)]
                   (boolean (and (mongo-db/insert-document! "user_accounts" user-account  {:prototype-f (prototype-f :user-account)})
                                 (mongo-db/insert-document! "user_profiles" user-profile  {:prototype-f (prototype-f :user-profile)})
                                 (mongo-db/insert-document! "user_settings" user-settings {:prototype-f (prototype-f :user-settings)})))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/add-user! {...}]
(a/reg-fx :user/add-user! add-user!)
