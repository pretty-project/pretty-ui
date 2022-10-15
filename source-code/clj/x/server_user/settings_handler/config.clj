
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def PUBLIC-USER-SETTINGS-PROJECTION {:user-settings/added-at    0 :user-settings/added-by    0
                                      :user-settings/modified-at 0 :user-settings/modified-by 0
                                      :user-settings/permissions 0})

; @constant (string)
(def DEFAULT-USER-SETTINGS-FILEPATH "environment/x.default-user-settings.edn")

; @constant (map)
; XXX#5890 (x.server-user.settings-handler.side-effects)
;
; A REQUIRED-USER-SETTINGS térkép tartalmazza az rendszer működéséhez
; nélkülözhetetlen beállításokat.
(def REQUIRED-USER-SETTINGS {:notification-bubbles-enabled? true
                             :notification-sounds-enabled?  false
                             :sending-error-reports?        true
                             :selected-language             :en
                             :selected-theme                :light
                             :timezone-offset               0})
