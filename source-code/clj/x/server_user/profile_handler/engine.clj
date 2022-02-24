
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler.engine
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.mid-user.profile-handler.engine :as profile-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.profile-handler.engine
(def MAX-FIRST-NAME-LENGTH profile-handler.engine/MAX-FIRST-NAME-LENGTH)
(def MAX-LAST-NAME-LENGTH  profile-handler.engine/MAX-LAST-NAME-LENGTH)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
;  {:user-profile/birthday (string)
;   :user-profile/first-name (string)
;   :user-profile/last-name (string)}
(def ANONYMOUS-USER-PROFILE {:user-profile/birthday   "1969-04-20"
                             :user-profile/first-name "Guest"
                             :user-profile/last-name  "User"})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-profile
  ; @param (string) user-account-id
  ;
  ; @usage
  ;  (user/user-account-id->user-profile "my-account")
  ;
  ; @return (map)
  [user-account-id]
  (local-db/get-document "user_profiles" user-account-id
                         {:additional-namespace :user-profile}))

(defn request->user-profile
  ; @param (map) request
  ;
  ; @usage
  ;  (r user/request->user-profile-item {...})
  ;
  ; @return (map)
  [request]
  (if-let [account-id (http/request->session-param request :user-account/id)]
          (user-account-id->user-profile account-id)
          (return ANONYMOUS-USER-PROFILE)))

(defn request->user-profile-item
  ; @param (map) request
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r user/request->user-profile-item {...} :email-address)
  ;
  ; @return (*)
  [request item-key]
  (let [user-profile        (request->user-profile request)
        namespaced-item-key (keyword/add-namespace :user-account item-key)]
       (get user-profile namespaced-item-key)))
