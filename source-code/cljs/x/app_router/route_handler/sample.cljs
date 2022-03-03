
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.sample
    (:require [x.app-core.api   :as a]
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

(a/reg-event-fx
  :go-my-route!
  ; A [:router/go-to! "/..."] esemény meghívásával a kliens-oldali útvonal-kezelő az esemény
  ; számára paraméterként átadott útvonalra irányít át.
  [:router-go-to! "/my-route"])



;; -- "@app-home" hivatkozás használata ---------------------------------------
;; ----------------------------------------------------------------------------

; A [:router/go-home!] esemény meghívásával a kliens-oldali útvonal-kezelő az x.app-config.edn
; fájlban {:app-home "/..."} tulajdonságként beállított útvonalra irányít át.
(a/reg-event-fx
  :go-my-home!
  [:router/go-home!])

; Az útvonalban használt "/@app-home" részt, az útvonal-kezelő behelyettesíti
; az x.app-config.edn fájlban {:app-home "/..."} tulajdonságként beállított útvonal értékével.
(a/reg-event-fx
  :go-my-route!
  [:router-go-to! "/@app-home/your-route"])



;; -- :route-parent beállítás használata --------------------------------------
;; ----------------------------------------------------------------------------

; A [:router/go-up!] esemény meghívásával a kliens-oldali útvonal-kezelő
; az aktuális útvonal {:route-parent "/..."} tulajdonságaként beállított útvonalra irányít át.
(a/reg-event-fx
  :go-my-parent!
  [:router/go-up!])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :go-my-back!
  ; TODO ...
  [:router/go-back!])
