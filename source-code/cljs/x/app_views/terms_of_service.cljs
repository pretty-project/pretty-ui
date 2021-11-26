
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.11
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.terms-of-service
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [surface-id])
  ; Multilingual content
  ;
  ; WARNING!
  ; Az Felhasználási feltételek tartalmát jelenítsd meg a cookie-consent popup felületen,
  ; ahelyett, hogy erre az oldalra irányítanád a terms-of-service gombbal a felhasználót!
  ; Erről az oldalról tovább lehet navigálni az applikáció más részire anélkül, hogy
  ; elfogadná a cookie-consent tartalmát!



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-terms-of-service!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:views/render-error-page! :under-construction])
