
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.install-handler.side-effects
    (:require [mongo-db.api                            :as mongo-db]
              [re-frame.api                            :as r]
              [x.server-user.user-handler.side-effects :as user-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (when (mongo-db/collection-empty? "user_accounts")
        (user-handler.side-effects/add-user! {:email-address "root@monotech.hu"
                                              :password      "Monotech.420"
                                              :first-name    "Tech"
                                              :last-name     "Mono"
                                              :pin           0000
                                              :roles         ["root"]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :user/check-install! check-install!)
