
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
  ; XXX#5890
  ; Az alapértelmezett felhasználói beállítások ...
  ; ... részét képezi a rendszer működéséhez nélkülözhetetlen beállítások (pl. :selected-theme, ...)
  ;
  ; Az alapértelmezetten kiválasztott nyelv értékének forrása az applikáció alapértelmezett nyelve.
  (let [default-user-settings (io/read-edn-file settings-handler.config/DEFAULT-USER-SETTINGS-FILEPATH)
        app-locale           @(r/subscribe [:core/get-app-config-item :app-locale])]
       (r/dispatch [:db/set-item! [:user :settings-handler/default-user-settings]
                                  (merge settings-handler.config/REQUIRED-USER-SETTINGS default-user-settings
                                         {:selected-language app-locale})])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :user/import-default-user-settings! import-default-user-settings!)
