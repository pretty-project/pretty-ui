
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.login-handler.transfer
    (:require [mid-fruits.map                      :as map]
              [x.server-core.api                   :as x.core]
              [x.server-user.login-handler.helpers :as login-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-transfer! :user/transfer-user-login!
  {:data-f      #(-> % login-handler.helpers/request->public-user-login map/remove-namespace)
   :target-path [:user :login-handler/user-login]})
