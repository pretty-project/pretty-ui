
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.12
; Description:
; Version: v0.5.8
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.installer
    (:require [mongo-db.api      :as mongo-db]
              [x.server-core.api :as a :refer [r]]
              [x.server-user.user-handler :as user-handler]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (when (mongo-db/collection-empty? "user_accounts")
        (user-handler/add-user! {:email-address "demo@monotech.hu"
                                 :password      "mono"
                                 :first-name    "Tech"
                                 :last-name     "Mono"})))

(a/reg-fx :user/check-install! check-install!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-launch {:user/check-install! nil}})
