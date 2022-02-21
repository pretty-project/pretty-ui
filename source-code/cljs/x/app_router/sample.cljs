
(ns x.app-router.sample
    (:require [x.app-core.api   :as a]
              [x.app-router.api :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :go-my-route!
  ; A [:router/go-to! "/..."] esemény meghívásával a kliens-oldali útvonal-kezelő az esemény
  ; számára paraméterként átadott útvonalra irányít át.
  [:router-go-to! "/my-route"])



;; -- "@app-home" hivatkozás használata ---------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :go-my-home!
  ; A [:router/go-home!] esemény meghívásával a kliens-oldali útvonal-kezelő az x.app-config.edn
  ; fájlban {:app-home "/..."} tulajdonságként beállított útvonalra irányít át.
  [:router/go-home!])

(a/reg-event-fx
  :go-my-route!
  ; Az útvonalban használt "/@app-home" részt, az útvonal-kezelő behelyettesíti
  ; az x.app-config.edn fájlban {:app-home "/..."} tulajdonságként beállított útvonal értékével.
  [:router-go-to! "/@app-home/your-route"])



;; -- :route-parent beállítás használata --------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :go-my-parent!
  ; A [:router/go-up!] esemény meghívásával a kliens-oldali útvonal-kezelő
  ; az aktuális útvonal {:route-parent "/..."} tulajdonságaként beállított útvonalra irányít át.
  [:router/go-up!])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :go-my-back!
  ; TODO ...
  [:router/go-back!])
