
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.6.8
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param return]]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [x.mid-user.profile-handler :as profile-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.profile-handler
(def MAX-FIRST-NAME-LENGTH profile-handler/MAX-FIRST-NAME-LENGTH)
(def MAX-LAST-NAME-LENGTH  profile-handler/MAX-LAST-NAME-LENGTH)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
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
  ; @return (map)
  [request]
  (if-let [account-id (http/request->session-param request :user-account/id)]
          (user-account-id->user-profile account-id)
          (return ANONYMOUS-USER-PROFILE)))

(defn request->user-profile-item
  ; @param (map) request
  ; @param (keyword) item-id
  ;
  ; @return (map)
  [request item-id]
  (let [user-profile (db/document->non-namespaced-document (request->user-profile request))]
       (get user-profile item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- transfer-user-profile
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (-> request request->user-profile db/document->pure-document))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:core/reg-transfer! :user/transfer-user-profile!
                                        {:data-f      transfer-user-profile
                                         :target-path [:user/profile :data-items]}]})
