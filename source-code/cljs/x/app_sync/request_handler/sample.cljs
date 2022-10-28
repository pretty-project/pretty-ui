
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.sample
    (:require [mid-fruits.candy :refer [return]]
              [re-frame.api     :as r :refer [r]]
              [x.app-sync.api   :as x.sync]))



;; -- Lekérés küldése ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :send-my-request!
  [:sync/send-request! :my-request
                       {}])



;; -- Szerver válaszának eltárolása saját függvénnyel -------------------------
;; ----------------------------------------------------------------------------

; A {:response-f ...} tulajdonságként átadott függvény paraméterként megkapja
; a szerver válaszát, és a függvény visszatérési értéke aktualizálja a Re-Frame
; adatbázist.
;
; XXX#0451
; A response-f függvény minden esetben megkapja paraméterként a Re-Frame adatbázist,
; a request-id azonosítót és a szerver-válaszát!
; Így biztosítható, hogy az adatbázis a szerver-válasz megérkezésekori állapotában
; legyen jelen a függvényben, ne pedig az elküldésekori állapotában.

(defn store-my-response!
  [db [_ request-id server-response]]
  (assoc-in db [:my-response] server-response))

(r/reg-event-fx :send-my-request!
  (fn [{:keys [db]} _]
      [:sync/send-request! :my-request
                           {:uri "/my-uri"
                            :response-f store-my-response!}]))



;; -- A szerver válaszának ellenőrzése ----------------------------------------
;; ----------------------------------------------------------------------------

; A {:validator-f ...} tulajdonságként átadott függvény paraméterként megkapja
; a szerver válaszát, és ha a függvény visszatérési értéke boolean típusként
; kiértékelve false érték, akkor az {:on-failure [...]} tulajdonságként átadott
; esemény történik meg, abban az esetben is ha a szerver válaszának HTTP státuszkódja
; ezt nem indolkolná!
;
; XXX#0451
; A validator-f függvény csak a szerver-válaszát kapja meg paraméterként,
; ha a függvényben szükséged van az adatbázisra, akkor a második példa szerint
; át kell adnod azt a függvénynek!
; Így biztosítható, hogy egyszerű függvények is alkalmazhatók legyenek validator-f
; függvényként (pl. string?, map? ...)

(r/reg-event-fx :send-my-validated-request!
  [:sync/send-request! :my-request
                       {:uri "/my-uri"
                        :on-success []
                        :on-failure []
                        :validator-f string?}])

(defn my-response-valid?
  [db [_ request-id server-response]]
  (return true))

(r/reg-event-fx :send-my-request!
  (fn [{:keys [db]} _]
      [:sync/send-request! :my-request
                           {:uri "/my-uri"
                            :on-success []
                            :on-failure []
                            :validator-f #(r my-response-valid? db :my-request %)}]))
