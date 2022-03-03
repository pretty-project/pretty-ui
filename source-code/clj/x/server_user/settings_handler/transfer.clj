
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.transfer
    (:require [x.server-core.api                     :as a]
              [x.server-db.api                       :as db]
              [x.server-user.settings-handler.engine :as settings-handler.engine]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer! :user/transfer-user-settings!
                 {:data-f      #(-> % settings-handler.engine/request->user-settings db/document->pure-document)
                  :target-path [:user :settings-handler/data-items]})
