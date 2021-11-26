

; Ez a webhely Önnel kapcsolatos adatokat nem gyűjt és harmadik félnek nem ad át.
;
; Cookie:
; Kizárólag a webhely működéséhez szükséges technológiai adatokat tárol
; Pl.: verziószám, ...
;
; Localstorage:
;

; Statisztikai adatgyüjtés, amikor nincs hozzákötve felhasználóhoz az adat. (anonimizált adat)
;
; https://ec.europa.eu/info/law/law-topic/data-protection/reform/rules-business-and-organisations/principles-gdpr/what-information-must-be-given-individuals-whose-data-collected_hu

; WEBSITE
;
; Nem tárolunk szinte semmit, inkább csak third party dolgok vannak.
; Nem átadjuk, hanem lehetővé tesszük az adatgyűjtést
; Google Maps, Youtube, Google Analytics, Facebook Pixel, Hotjar
; Cross-site trackers

; APPLICATION
;
; Hotjar (szükséges a public appikációkhoz)
;
; https://ec.europa.eu/info/law/law-topic/data-protection/reform/rights-citizens/how-my-personal-data-protected/can-personal-data-about-children-be-collected_hu



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.04
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.privacy-policy
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
  ; Remove stored cookies button
  ; Multilingual content
  ;
  ; WARNING!
  ; Az Adatvédelmi irányelvek tartalmát jelenítsd meg a cookie-consent popup felületen,
  ; ahelyett, hogy erre az oldalra irányítanád a privacy-policy gombbal a felhasználót!
  ; Erről az oldalról tovább lehet navigálni az applikáció más részire anélkül, hogy
  ; elfogadná a cookie-consent tartalmát!



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-privacy-policy!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:views/render-error-page! :under-construction])
