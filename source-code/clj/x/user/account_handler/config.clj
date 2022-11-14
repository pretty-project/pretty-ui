
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.account-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def PUBLIC-USER-ACCOUNT-PROJECTION {:user-account/added-at    0 :user-account/added-by    0
                                     :user-account/modified-at 0 :user-account/modified-by 0
                                     :user-account/password    0 :user-account/permissions 0
                                     :user-account/pin         0})

; @constant (namespaced map)
;  {:user-account/email-address (nil)
;   :user-account/id (nil)
;   :user-account/roles (strings in vector)}
(def ANONYMOUS-USER-ACCOUNT {:user-account/email-address nil
                             :user-account/id            nil
                             :user-account/roles         []})

; @constant (namespaced map)
;  {:user-account/email-address (nil)
;   :user-account/id (string)
;   :user-account/roles (strings in vector)}
(def SYSTEM-USER-ACCOUNT {:user-account/email-address nil
                          :user-account/id            "system"
                          :user-account/roles         []})
