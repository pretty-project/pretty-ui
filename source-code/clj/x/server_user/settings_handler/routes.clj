
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.routes
    (:require [local-db.api                          :as local-db]
              [mid-fruits.keyword                    :as keyword]
              [server-fruits.http                    :as http]
              [x.server-user.account-handler.engine  :as account-handler.engine]
              [x.server-user.settings-handler.engine :as settings-handler.engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upload-user-settings-item!
  ; @param (map) request
  ;  {:params {:item-id (keyword)
  ;            :item (*)}}
  ;
  ; @return (map)
  [request]
  (if (account-handler.engine/request->authenticated? request)
      ; If user authenticated ...
      (let [user-id    (http/request->session-param request :user-account/id)
            item-key   (http/request->param         request :item-key)
            item-value (http/request->param         request :item-value)
            namespaced-item-key (keyword/add-namespace :user-settings item-key)
            default-value       (get settings-handler.engine/ANONYMOUS-USER-SETTINGS namespaced-item-key)]
           (if (= item-value default-value)
               (local-db/update-document! "user_settings" user-id dissoc item-key)
               (local-db/update-document! "user_settings" user-id assoc  item-key item-value))
           (http/text-wrap {:body "Uploaded"}))
      ; If user is NOT authenticated ...
      (http/error-wrap {:error-message :permission-denied :status 401})))
