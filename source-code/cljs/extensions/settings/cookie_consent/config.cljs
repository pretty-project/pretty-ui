
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.cookie-consent.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  BUG#2457
;  A cookie-consent popup nem renderelődhet ki a legelőször kirenderelt surface előtt!
(def BOOT-RENDERING-DELAY 1000)
