
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler.transfer
    (:require [mid-fruits.map                        :as map]
              [x.server-core.api                     :as core]
              [x.server-user.profile-handler.helpers :as profile-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-transfer! :user/transfer-user-profile!
  {:data-f      #(-> % profile-handler.helpers/request->public-user-profile map/remove-namespace)
   :target-path [:user :profile-handler/user-profile]})
