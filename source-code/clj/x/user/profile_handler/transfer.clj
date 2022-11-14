
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.profile-handler.transfer
    (:require [mid-fruits.map                 :as map]
              [x.core.api                     :as x.core]
              [x.user.profile-handler.helpers :as profile-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-transfer! :x.user/transfer-user-profile!
  {:data-f      #(-> % profile-handler.helpers/request->public-user-profile map/remove-namespace)
   :target-path [:x.user :profile-handler/user-profile]})
