
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.transfer
    (:require [mid-fruits.map                         :as map]
              [x.server-core.api                      :as x.core]
              [x.server-user.settings-handler.helpers :as settings-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-transfer! :user/transfer-user-settings!
  {:data-f      #(-> % settings-handler.helpers/request->public-user-settings map/remove-namespace)
   :target-path [:user :settings-handler/user-settings]})
