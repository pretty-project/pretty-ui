
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.transfer
    (:require [x.server-core.api                    :as a]
              [x.server-db.api                      :as db]
              [x.server-user.account-handler.engine :as account-handler.engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer! :user/transfer-user-account!
                 {:data-f      #(-> % account-handler.engine/request->user-public-account db/document->non-namespaced-document)
                  :target-path [:user :account-handler/data-items]})
