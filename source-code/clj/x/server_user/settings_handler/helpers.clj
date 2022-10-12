
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.helpers
    (:require [mongo-db.api                          :as mongo-db]
              [mid-fruits.candy                      :refer [return]]
              [mid-fruits.keyword                    :as keyword]
              [re-frame.api                          :as r]
              [server-fruits.http                    :as http]
              [server-fruits.io                      :as io]
              [x.server-user.settings-handler.config :as settings-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-settings
  ; @param (string) user-account-id
  ;
  ; @usage
  ;  (user/user-account-id->user-settings "my-account")
  ;
  ; @return (namespaced map)
  [user-account-id]
  ; Az anonymous felhasználó beállításai ...
  (merge @(r/subscribe [:user/get-default-user-settings])
          (mongo-db/get-document-by-id "user_settings" user-account-id)))

(defn user-account-id->user-settings-item
  ; @param (string) user-account-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user/user-account-id->user-settings-item "my-account" :selected-language)
  ;
  ; @return (*)
  [user-account-id item-key]
  (let [user-settings       (user-account-id->user-settings user-account-id)
        namespaced-item-key (keyword/add-namespace :user-settings item-key)]
       (get user-settings namespaced-item-key)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-settings
  ; @param (map) request
  ;
  ; @usage
  ;  (user/request->user-settings {...})
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (merge @(r/subscribe [:user/get-default-user-settings])
                  (mongo-db/get-document-by-id "user_settings" user-account-id))
          (merge @(r/subscribe [:user/get-default-user-settings]))))

(defn request->public-user-settings
  ; @param (map) request
  ;
  ; @usage
  ;  (user/request->user-settings {...})
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (merge @(r/subscribe [:user/get-default-user-settings])
                  (mongo-db/get-document-by-id "user_settings" user-account-id settings-handler.config/PUBLIC-USER-SETTINGS-PROJECTION))
          (merge @(r/subscribe [:user/get-default-user-settings]))))

(defn request->user-settings-item
  ; @param (map) request
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user/request->user-settings-item {...} :selected-language)
  ;
  ; @return (map)
  [request item-key]
  (let [user-settings       (request->user-settings request)
        namespaced-item-key (keyword/add-namespace :user-settings item-key)]
       (get user-settings namespaced-item-key)))
