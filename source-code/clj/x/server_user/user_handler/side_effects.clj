
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler.side-effects
    (:require [mongo-db.api                           :as mongo-db]
              [re-frame.api                           :as r]
              [x.server-user.account-handler.config   :as account-handler.config]
              [x.server-user.document-handler.helpers :as document-handler.helpers]
              [x.server-user.user-handler.helpers     :as user-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-user!
  ; @param (string)(opt) user-id
  ; @param (map) user-props
  ;  {:email-address (string)
  ;   :first-name (string)
  ;   :password (string)
  ;   :last-name (string)
  ;   :roles (strings in vector)(opt)}
  ;
  ; @usage
  ;  (add-user! {...})
  ;
  ; @return (boolean)
  ([user-props]
   (add-user! (mongo-db/generate-id) user-props))

  ([user-id {:keys [email-address password] :as user-props}]
   (if (user-handler.helpers/user-props-valid? user-props)
       (if-not (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address email-address})
               (let [prepare-f    #(document-handler.helpers/added-document-prototype {:session account-handler.config/SYSTEM-USER-ACCOUNT} %)
                     user-account  (user-handler.helpers/user-props->user-account  user-id user-props)
                     user-profile  (user-handler.helpers/user-props->user-profile  user-id user-props)
                     user-settings (user-handler.helpers/user-props->user-settings user-id user-props)]
                    (and (mongo-db/insert-document! "user_accounts" user-account  {:prepare-f prepare-f})
                         (mongo-db/insert-document! "user_profiles" user-profile  {:prepare-f prepare-f})
                         (mongo-db/insert-document! "user_settings" user-settings {:prepare-f prepare-f})))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/add-user! {...}]
;
; @usage
;  [:user/add-user! "my-user" {...}]
(r/reg-fx :user/add-user! add-user!)
