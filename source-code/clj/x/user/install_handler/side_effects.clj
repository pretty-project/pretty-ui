
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.install-handler.side-effects
    (:require [mongo-db.api                     :as mongo-db]
              [re-frame.api                     :as r]
              [x.user.user-handler.side-effects :as user-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (if-let [_ (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address "root@monotech.hu"})]
          ()
          (user-handler.side-effects/add-user! {:email-address "root@monotech.hu"
                                                :password      "Monotech.420"
                                                :first-name    "Tech"
                                                :last-name     "Mono"
                                                :pin           0000
                                                :roles         ["root"]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.user/check-install! check-install!)
