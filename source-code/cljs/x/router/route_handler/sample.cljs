
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.sample
    (:require [re-frame.api :as r]
              [x.router.api :as x.router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :go-to-my-route!
  ; A [:x.router/go-to! "/..."] esemény meghívásával a kliens-oldali útvonal-kezelő az esemény
  ; számára paraméterként átadott útvonalra irányít át.
  [:x.router-go-to! "/my-route"])

(r/reg-event-fx :go-to-your-route!
  ; A {:parent-route "..."} beállítás használatával beállítható, hogy az útvonal
  ; szülő-útvonala, eltérjen az eredetileg beállított szülő-útvonaltól.
  [:x.router-go-to! "/your-route" {:parent-route "/my-route"}])



;; -- "@app-home" hivatkozás használata ---------------------------------------
;; ----------------------------------------------------------------------------

; A [:x.router/go-home!] esemény meghívásával a kliens-oldali útvonal-kezelő az x.app-config.edn
; fájlban {:app-home "/..."} tulajdonságként beállított útvonalra irányít át.
(r/reg-event-fx :go-to-my-home!
  [:x.router/go-home!])

; Az útvonalban használt "/@app-home" részt, az útvonal-kezelő behelyettesíti
; az x.app-config.edn fájlban {:app-home "/..."} tulajdonságként beállított útvonal értékével.
(r/reg-event-fx :go-to-my-route!
  [:x.router-go-to! "/@app-home/your-route"])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :my-go-back!
  ; TODO ...
  [:x.router/go-back!])



;; -- Az útvonal lecserélése hatások nélkül -----------------------------------
;; ----------------------------------------------------------------------------

; A [:x.router/swap-to! ...] esemény lecseréli az aktuálisan használt útvonalat,
; a paraméterként kapott útvonalra, az útvonalhoz rendelt események figyelmen
; kívül hagyásával.
;
; A [:x.router/swap-to! ...] esemény az útvonal-kezelőt {:swap-mode? true}
; állapotba lépteti, amely állapotban az útvonalkezelő felismeri, hogy
; az útvonalhoz rendelt események figyelmen kívül hagyhatók.
(r/reg-event-fx :swap-my-route!
  [:x.router/swap-to! "/@app-home/my-route"])
