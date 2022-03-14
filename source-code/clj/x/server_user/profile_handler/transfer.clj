
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler.transfer
    (:require [x.server-core.api                     :as a]
              [x.server-db.api                       :as db]
              [x.server-user.profile-handler.helpers :as profile-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer! :user/transfer-user-profile!
                 {:data-f      #(-> % profile-handler.helpers/request->user-profile db/document->pure-document)
                  :target-path [:user :profile-handler/data-items]})
