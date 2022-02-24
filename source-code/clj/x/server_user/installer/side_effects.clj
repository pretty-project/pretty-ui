
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.installer.side-effects
    (:require [mongo-db.api      :as mongo-db]
              [x.server-core.api :as a]
              [x.server-user.user-handler.side-effects :as user-handler.side-effects]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (when (mongo-db/collection-empty? "user_accounts")
        (user-handler.side-effects/add-user! {:email-address "demo@monotech.hu"
                                              :password      "mono"
                                              :first-name    "Tech"
                                              :last-name     "Mono"})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :user/check-install! check-install!)
