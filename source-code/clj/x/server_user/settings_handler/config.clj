
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

; @constant (namespaced map)
;  {:user-settings/notification-bubbles-enabled? (boolean)
;   :user-settings/notification-sounds-enabled (boolean)
;   :user-settings/sending-error-reports? (boolean)
;   :user-settings/selected-language (keyword)
;   :user-settings/selected-theme (keyword)
;   :user-settings/timezone-offset (?)}
(def DEFAULT-USER-SETTINGS {:user-settings/notification-bubbles-enabled? true
                            :user-settings/notification-sounds-enabled?  false
                            :user-settings/sending-error-reports?        true
                            :user-settings/selected-language             :en
                            :user-settings/selected-theme                :light
                            :user-settings/timezone-offset               0})
