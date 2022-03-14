
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.cookie-settings.effects
    (:require [extensions.settings.cookie-settings.views :as cookie-settings.views]
              [x.app-core.api                            :as a]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; WARNING#0459



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.cookie-settings/render-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! :settings.cookie-settings/view
                  {:body   #'cookie-settings.views/body
                   :header #'cookie-settings.views/header
                   :horizontal-align :left
                   :user-close?      false}])
