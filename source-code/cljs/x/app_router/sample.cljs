
(ns x.app-router.sample
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A [:router/go-home!] esemény meghívásával a kliens-oldali útvonal-kezelő
; az x.project-config.edn fájlban {:app-home "/..."} tulajdonságként beállított
; útvonalra irányít át.
(a/dispatch [:router/go-home!])

; A [:router/go-up!] esemény meghívásával a kliens-oldali útvonal-kezelő
; az aktuális útvonal {:route-parent "/..."} tulajdonságaként megadott útvonalra irányít át.
(a/dispatch [:router/go-up!])

; TODO ...
(a/dispatch [:router/go-back!])

; A [:router/go-to! "/..."] esemény meghívásával a kliens-oldali útvonal-kezelő az esemény számára
; paraméterként átadott útvonalra irányít át.
(a/dispatch [:router-go-to! "/my-route"])

; Az útvonalban használt "/:app-home" részt, az útvonal-kezelő behelyettesíti
; az x.project-config.edn fájlban {:app-home "/..."} tulajdonságként beállított útvonal értékével.
(a/dispatch [:router-go-to! "/:app-home/your-route"])
