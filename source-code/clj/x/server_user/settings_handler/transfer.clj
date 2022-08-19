

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.transfer
    (:require [x.server-core.api                      :as a]
              [x.server-db.api                        :as db]
              [x.server-user.settings-handler.helpers :as settings-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer! :user/transfer-user-settings!
                 {:data-f      #(-> % settings-handler.helpers/request->user-settings db/document->pure-document)
                  :target-path [:user :settings-handler/data-items]})
