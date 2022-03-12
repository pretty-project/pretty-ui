
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.config)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (nil)
;  If Domain is unspecified, the attribute defaults to the same host that set the cookie, excluding subdomains.
;  If Domain is specified, then subdomains are always included.
(def COOKIE-DOMAIN nil)

; @constant (string)
(def COOKIE-PATH "/")

; @constant (map)
(def COOKIE-NAME-PREFIXES {:analytics       "xa"
                           :necessary       "xn"
                           :user-experience "xue"})

; @constant (map)
(def COOKIE-TYPES {"xa"  :analytics
                   "xn"  :necessary
                   "xue" :user-experience})
