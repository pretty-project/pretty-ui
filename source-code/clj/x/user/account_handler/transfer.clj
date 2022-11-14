
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.account-handler.transfer
    (:require [mid-fruits.map                 :as map]
              [x.core.api                     :as x.core]
              [x.user.account-handler.helpers :as account-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-transfer! :x.user/transfer-user-account!
  {:data-f      #(-> % account-handler.helpers/request->public-user-account map/remove-namespace)
   :target-path [:x.user :account-handler/user-account]})
