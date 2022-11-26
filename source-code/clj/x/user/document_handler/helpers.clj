
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.document-handler.helpers
    (:require [candy.api                      :refer [param return]]
              [http.api                       :as http]
              [map.api                        :as map]
              [time.api                       :as time]
              [x.user.account-handler.helpers :as account-handler.helpers]
              [x.user.profile-handler.helpers :as profile-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (map) request
  ; @param (namespaced map) document
  ;
  ; @usage
  ;  (added-document-prototype {} {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;    {:user/account-id (string)}
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)
  ;    {:user/account-id (string)}}
  [request document]
  (if-let [namespace (-> document map/get-namespace name)]
          (let [timestamp (time/timestamp-string)
                user-link (account-handler.helpers/request->user-link request)]
               (merge document {(keyword namespace "added-at")    timestamp
                                (keyword namespace "added-by")    user-link
                                (keyword namespace "modified-at") timestamp
                                (keyword namespace "modified-by") user-link}))))

(defn updated-document-prototype
  ; @param (map) request
  ; @param (namespaced map) document
  ;
  ; @usage
  ;  (updated-document-prototype {} {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;    {:user/account-id (string)}
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)
  ;    {:user/account-id (string)}}
  [request document]
  (if-let [namespace (-> document map/get-namespace name)]
          (let [timestamp (time/timestamp-string)
                user-link (account-handler.helpers/request->user-link request)]
               (merge {(keyword namespace "added-at") timestamp
                       (keyword namespace "added-by") user-link}
                      (param document)
                      {(keyword namespace "modified-at") timestamp
                       (keyword namespace "modified-by") user-link}))))

(defn duplicated-document-prototype
  ; @param (map) request
  ; @param (namespaced map) document
  ;
  ; @usage
  ;  (updated-document-prototype {} {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;    {:user/account-id (string)}
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)
  ;    {:user/account-id (string)}}
  [request document]
  (if-let [namespace (-> document map/get-namespace name)]
          (let [timestamp (time/timestamp-string)
                user-link (account-handler.helpers/request->user-link request)]
               (merge document {(keyword namespace "added-at")    timestamp
                                (keyword namespace "added-by")    user-link
                                (keyword namespace "modified-at") timestamp
                                (keyword namespace "modified-by") user-link}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fill-document
  ; @param (map) request
  ; @param (namespaced map) document
  ;
  ; @usage
  ;  (fill-document {...} {...})
  ;
  ; @example
  ;  (fill-document {...} {:namespace/added-by {:user-account/id "my-user"}})
  ;  =>
  ;  {:namespace/added-by {:user-account/id         "my-user"
  ;                        :user-profile/first-name "My"
  ;                        :user-profile/last-name  "User"}}
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-by (map)
  ;    {:user-account/id (string)
  ;     :user-profile/first-name (string)
  ;     :user-profile/last-name (string)}
  ;   :namespace/modified-by (map)
  ;    {:user-account/id (string)
  ;     :user-profile/first-name (string)
  ;     :user-profile/last-name (string)}}
  [_ document]
  (if-let [namespace (-> document map/get-namespace name)]
          (as-> document % (if-let [user-account-id (get-in document [(keyword namespace "added-by") :user-account/id])]
                                   (update % (keyword namespace "added-by") merge
                                             {:user-profile/first-name (profile-handler.helpers/user-account-id->user-profile-item user-account-id :first-name)
                                              :user-profile/last-name  (profile-handler.helpers/user-account-id->user-profile-item user-account-id :last-name)})
                                   (return %))
                           (if-let [user-account-id (get-in document [(keyword namespace "modified-by") :user-account/id])]
                                   (update % (keyword namespace "modified-by") merge
                                             {:user-profile/first-name (profile-handler.helpers/user-account-id->user-profile-item user-account-id :first-name)
                                              :user-profile/last-name  (profile-handler.helpers/user-account-id->user-profile-item user-account-id :last-name)})
                                   (return %)))))

(defn clean-document
  ; @param (map) request
  ; @param (namespaced map) document
  ;
  ; @usage
  ;  (clean-document {...} {...})
  ;
  ; @example
  ;  (clean-document {...} {:namespace/added-by {:user-account/id         "my-user"
  ;                                              :user-profile/first-name "My"
  ;                                              :user-profile/last-name  "User"})
  ;  =>
  ;  {:namespace/added-by {:user-account/id "my-user"}}
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-by (map)
  ;    {:user-account/id (string)}
  ;   :namespace/modified-by (map)
  ;    {:user-account/id (string)}}
  [_ document]
  (if-let [namespace (-> document map/get-namespace name)]
          (as-> document % (if-let [user-account-id (get-in document [(keyword namespace "added-by") :user-account/id])]
                                   (assoc  % (keyword namespace "added-by") {:user-account/id user-account-id})
                                   (return %))
                           (if-let [user-account-id (get-in document [(keyword namespace "modified-by") :user-account/id])]
                                   (assoc  % (keyword namespace "modified-by") {:user-account/id user-account-id})
                                   (return %)))))
