
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.side-effects
    (:require [re-frame.api                          :as r]
              [server-fruits.io                      :as io]
              [x.server-user.settings-handler.config :as settings-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-default-user-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (r/dispatch [:db/set-item! [:user :settings-handler/default-user-settings]
                             (io/read-edn-file settings-handler.config/DEFAULT-USER-SETTINGS-FILEPATH)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :user/import-default-user-settings! import-default-user-settings!)
