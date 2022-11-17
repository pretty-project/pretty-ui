
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.login-handler.transfer
    (:require [map.api                      :as map]
              [x.core.api                   :as x.core]
              [x.user.login-handler.helpers :as login-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-transfer! :x.user/transfer-user-login!
  {:data-f      #(-> % login-handler.helpers/request->public-user-login map/remove-namespace)
   :target-path [:x.user :login-handler/user-login]})
