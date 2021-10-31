
(ns x.app-controller.controll-handler
    (:require []))


(a/reg-event-fx
  :x.app-controller/use-controller!
  ; @param ()
  ; @param (map)
  ;  {:query (string or vector)
  ;   :on-failure (metamorphic-event)(opt)
  ;   :on-success (metamorphic-event)(opt)
  ;   :on-
  ;   :route-string (string)(opt)
  ;   }
  (fn [{} [_ controller-id controller-props]]))


; Rákattintok egy gombra a UI-on
;
; 1. Átváltja a route-ot a címsorban (opt)
; 2. A request-indicator-t átállítja a request-id-ra (opt)
; 3. Ha nincs request 



; Ha beirom a cimsorba a linket (kívülről jövök), akkor a route-event-nek meg kell történjen
;
; 1. Route-ot beirod (Enter)
; 2. indicator beállítása
; 3. Indul a request
; 4. Render surface (A surface en megjelenik a ghost view)
; 5. Itt a valasz (nem on-success-re renderel! vagy igen és ignorálja?)
