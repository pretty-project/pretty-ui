
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler.transfer
    (:require [x.server-core.api                    :as a]
              [x.server-db.api                      :as db]
              [x.server-user.profile-handler.engine :as profile-handler.engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer! :user/transfer-user-profile!
                 {:data-f      #(-> % profile-handler.engine/request->user-profile db/document->pure-document)
                  :target-path [:user :profile-handler/data-items]})
