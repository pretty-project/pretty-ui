
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.api
    (:require [settings.appearance-settings.effects]
              [settings.appearance-settings.views]
              [settings.cookie-consent.effects]
              [settings.cookie-consent.lifecycles]
              [settings.cookie-consent.subs]
              [settings.cookie-consent.views]
              [settings.cookie-settings.effects]
              [settings.cookie-settings.views]
              [settings.notification-settings.views]
              [settings.personal-settings.views]
              [settings.privacy-settings.views]
              [settings.remove-stored-cookies.effects]
              [settings.remove-stored-cookies.views]
              [settings.view-selector.effects]
              [settings.view-selector.views]))
