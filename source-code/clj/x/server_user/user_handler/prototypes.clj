
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler.prototypes
    (:require [mongo-db.api                         :as mongo-db]
              [x.server-user.account-handler.config :as account-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn prototype-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespace) keyword
  ;
  ; @return (function)
  [namespace]
  #(mongo-db/added-document-prototype {:session account-handler.config/SYSTEM-ACCOUNT} namespace %))
