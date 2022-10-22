
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.sample
    (:require [re-frame.api     :as r]
              [x.app-router.api :as router]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#3387 ----------------------------------------------------------------

; @name route-string
;  "/products/big-green-bong?type=hit#order"
;
; @name route-path
;  "/products/big-green-bong"
;
; @name route-template
;  "/products/:product-id"
;
; @name route-tail
;  "type=hit#order"
;
; @name route-query-string
;  "type=hit"
;
; @name route-query-params
;  {"type" "hit"}
;
; @name route-fragment
;  "order"
;
; @name client-event
;  [:products/render!]
;
; @name route-id
;  :products/route



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :go-to-my-route!
  ; A [:router/go-to! "/..."] esemény meghívásával a kliens-oldali útvonal-kezelő az esemény
  ; számára paraméterként átadott útvonalra irányít át.
  [:router-go-to! "/my-route"])

(r/reg-event-fx :go-to-your-route!
  ; A {:route-parent "..."} beállítás használatával beállítható, hogy az útvonal
  ; szülő-útvonala, eltérjen az eredetileg beállított szülő-útvonaltól.
  [:router-go-to! "/your-route" {:route-parent "/my-route"}])



;; -- "@app-home" hivatkozás használata ---------------------------------------
;; ----------------------------------------------------------------------------

; A [:router/go-home!] esemény meghívásával a kliens-oldali útvonal-kezelő az x.app-config.edn
; fájlban {:app-home "/..."} tulajdonságként beállított útvonalra irányít át.
(r/reg-event-fx :go-my-home!
  [:router/go-home!])

; Az útvonalban használt "/@app-home" részt, az útvonal-kezelő behelyettesíti
; az x.app-config.edn fájlban {:app-home "/..."} tulajdonságként beállított útvonal értékével.
(r/reg-event-fx :go-my-route!
  [:router-go-to! "/@app-home/your-route"])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :go-my-back!
  ; TODO ...
  [:router/go-back!])



;; -- Az útvonal lecserélése hatások nélkül -----------------------------------
;; ----------------------------------------------------------------------------

; A [:router/swap-to! ...] esemény lecseréli az aktuálisan használt útvonalat,
; a paraméterként kapott útvonalra, az útvonalhoz rendelt események figyelmen
; kívül hagyásával.
;
; A [:router/swap-to! ...] esemény az útvonal-kezelőt {:swap-mode? true}
; állapotba lépteti, amely állapotban az útvonalkezelő felismeri, hogy
; az útvonalhoz rendelt események figyelmen kívül hagyhatók.
(r/reg-event-fx :swap-my-route!
  [:router/swap-to! "/@app-home/my-route"])
