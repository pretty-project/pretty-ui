
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.0
; Compatibility: x4.4.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler
    (:require [mid-fruits.candy           :refer [param return]]
              [mid-fruits.map             :as map]
              [mongo-db.api               :as mongo-db]
              [x.mid-user.account-handler :as account-handler]
              [x.server-core.api          :as a]
              [x.server-db.api            :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-props->user-account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (namespaced map)
  [user-props]
  (let [user-account (map/inherit user-props [:email-address :password :pin :roles])]
       (db/document->namespaced-document user-account "user-account")))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) user-props
  ;
  ; @return (map)
  ;  {:roles (vector)}
  [{:keys [] :as user-props}]
  (merge {:roles []}
         (param user-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-user!
  ; @param (map) user-props
  ;  {:email-address (string)
  ;   :password (string)
  ;   :pin (string)(opt)
  ;   :roles (strings in vector)(opt)}
  ;
  ; @usage
  ;  (user/add-user! {...})
  ;
  ; @return (namespaced map)
  ;  {}
  [user-props]
  (let [user-props   (a/prot user-props user-props-prototype)
        user-account (user-props->user-account user-props)]))
      ;(if (and (account-handler/user-account-valid? user-account))
           ; Check if email exists!!!
           ;(println (str user-account)))))
       ;(println (str (mongo-db/add-document! "user_accounts" user-account)))))

; @usage
;  [:x.server-user/add-user! {...}]
(a/reg-handled-fx :x.server-user/add-user! add-user!)
