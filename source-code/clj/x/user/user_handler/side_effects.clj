
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.user-handler.side-effects
    (:require [mongo-db.api                    :as mongo-db]
              [re-frame.api                    :as r]
              [x.user.account-handler.config   :as account-handler.config]
              [x.user.document-handler.helpers :as document-handler.helpers]
              [x.user.user-handler.helpers     :as user-handler.helpers]))



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
   (boolean (if (user-handler.helpers/user-props-valid? user-props)
                (if-not (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address email-address})
                        (let [prepare-f    #(document-handler.helpers/added-document-prototype {:session account-handler.config/SYSTEM-USER-ACCOUNT} %)
                              user-account  (user-handler.helpers/user-props->user-account  user-id user-props)
                              user-profile  (user-handler.helpers/user-props->user-profile  user-id user-props)
                              user-settings (user-handler.helpers/user-props->user-settings user-id user-props)]
                             (and (mongo-db/insert-document! "user_accounts" user-account  {:prepare-f prepare-f})
                                  (mongo-db/insert-document! "user_profiles" user-profile  {:prepare-f prepare-f})
                                  (mongo-db/insert-document! "user_settings" user-settings {:prepare-f prepare-f}))))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.user/add-user! {...}]
;
; @usage
;  [:x.user/add-user! "my-user" {...}]
(r/reg-fx :x.user/add-user! add-user!)
