

; PDF formátumú letölthető fájl lesz ez a view helyett



; Ez a webhely Önnel kapcsolatos adatokat nem gyűjt és harmadik félnek nem ad át.
;
; Cookie:
; Kizárólag a webhely működéséhez szükséges technológiai adatokat tárol
; Pl.: verziószám, ...
;
; Localstorage:




; Az oldal elején mindjárt egy toggle!
; "Az adatvédelmi beállításokat itt találád"
; {:dispatch [:x.app-router/go-to! "/privacy-settings"]}
;

; Statisztikai adatgyüjtés, amikor nincs hozzákötve felhasználóhoz az adat. (anonimizált adat)
;

;

; https://ec.europa.eu/info/law/law-topic/data-protection/reform/rules-business-and-organisations/principles-gdpr/what-information-must-be-given-individuals-whose-data-collected_hu

; WEBSITE
;
; Nem tárolunk szinte semmit, inkább csak third party dolgok vannak.
; Nem átadjuk, hanem lehetővé tesszük az adatgyűjtést
; Google Maps, Youtube, Google Analytics, Facebook Pixel, Hotjar
; Cross-site trackers
;



; APPLICATION
;
; Hotjar (kell az public appokba, hogy legyen visszajelzés)
;
;
; https://ec.europa.eu/info/law/law-topic/data-protection/reform/rights-citizens/how-my-personal-data-protected/can-personal-data-about-children-be-collected_hu



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.04
; Description:
; Version: v0.1.0
; Compatibility: x3.9.9



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
  ; @return (hiccup)
  []
  [elements/box {:content "Coming soon..."}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view}])

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-router/add-route!
   ::route
   {:route-event    [::render!]
    :route-template "/privacy-policy"}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [::initialize!]})
