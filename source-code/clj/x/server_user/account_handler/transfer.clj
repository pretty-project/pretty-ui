
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.transfer
    (:require [mid-fruits.map                        :as map]
              [x.server-core.api                     :as core]
              [x.server-user.account-handler.helpers :as account-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-transfer! :user/transfer-user-account!
  {:data-f      #(-> % account-handler.helpers/request->public-user-account map/remove-namespace)
   :target-path [:user :account-handler/user-account]})
