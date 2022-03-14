
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
;  A felhasználó account dokumentumának mely kulcsai kerülhetnek
;  a HTTP Session térképbe.
(def USER-PUBLIC-ACCOUNT-PROPS [:user-account/email-address :user-account/id :user-account/roles])

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
(def SYSTEM-ACCOUNT {:user-account/email-address nil
                     :user-account/id            "system"
                     :user-account/roles         []})
