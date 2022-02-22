
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.21
; Description:
; Version: 0.2.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.api
    (:require [x.app-environment.cookie-handler.effects]
              [x.app-environment.cookie-handler.events]
              [x.app-environment.cookie-handler.side-effects]
              [x.app-environment.cookie-handler.engine :as engine]
              [x.app-environment.cookie-handler.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-environment.cookie-handler.engine
(def cookie-setting-path engine/cookie-setting-path)

; x.app-environment.cookie-handler.subs
(def get-stored-cookies               subs/get-stored-cookies)
(def any-cookies-stored?              subs/any-cookies-stored?)
(def get-cookie-value                 subs/get-cookie-value)
(def cookies-enabled-by-browser?      subs/cookies-enabled-by-browser?)
(def analytics-cookies-enabled?       subs/analytics-cookies-enabled?)
(def necessary-cookies-enabled?       subs/necessary-cookies-enabled?)
(def user-experience-cookies-enabled? subs/user-experience-cookies-enabled?)
