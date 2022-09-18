
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.document-handler.helpers
    (:require [mid-fruits.candy                      :refer [param]]
              [mid-fruits.time                       :as time]
              [server-fruits.http                    :as http]
              [x.server-user.profile-handler.helpers :as profile-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-link
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:user-account/id (string)}
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          {:user-account/id user-account-id}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) updated-item
  ;
  ; @usage
  ;  (user/added-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;    {:user/account-id (string)}
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)
  ;    {:user/account-id (string)}}
  [request document-namespace updated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-string)
        user-link (request->user-link request)]
       (merge (param updated-item)
              {(keyword namespace "added-at")    timestamp
               (keyword namespace "added-by")    user-link
               (keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))

(defn updated-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) updated-item
  ;
  ; @usage
  ;  (user/updated-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;    {:user/account-id (string)}
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)
  ;    {:user/account-id (string)}}
  [request document-namespace updated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-string)
        user-link (request->user-link request)]
       (merge {(keyword namespace "added-at") timestamp
               (keyword namespace "added-by") user-link}
              (param updated-item)
              {(keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))

(defn duplicated-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) duplicated-item
  ;
  ; @usage
  ;  (user/updated-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;    {:user/account-id (string)}
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)
  ;    {:user/account-id (string)}}
  [request document-namespace duplicated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-string)
        user-link (request->user-link request)]
       (merge (param duplicated-item)
              {(keyword namespace "added-at")    timestamp
               (keyword namespace "added-by")    user-link
               (keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fill-document
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) item
  ;
  ; @usage
  ;  (user/fill-document {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-by (map)
  ;    {:user/account-id (string)
  ;     :user/first-name (string)
  ;     :user/last-name (string)}
  ;   :namespace/modified-by (map)
  ;    {:user/account-id (string)}
  ;     :user/first-name (string)
  ;     :user/last-name (string)}}
  [_ document-namespace item]
  (let [namespace   (name document-namespace)
        added-by    (get item (keyword namespace "added-by"))
        modified-by (get item (keyword namespace "modified-by"))]
       (-> item (update (keyword namespace "added-by") merge
                        (let [user-account-id (get added-by :user-account/id)]
                             {:user-profile/first-name (profile-handler.helpers/user-account-id->user-profile-item user-account-id :first-name)
                              :user-profile/last-name  (profile-handler.helpers/user-account-id->user-profile-item user-account-id :last-name)}))
                (update (keyword namespace "modified-by") merge
                        (let [user-account-id (get modified-by :user-account/id)]
                             {:user-profile/first-name (profile-handler.helpers/user-account-id->user-profile-item user-account-id :first-name)
                              :user-profile/last-name  (profile-handler.helpers/user-account-id->user-profile-item user-account-id :last-name)})))))
