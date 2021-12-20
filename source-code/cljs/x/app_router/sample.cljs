
(ns x.app-router.sample
    (:require [x.app-core.api   :as a]
              [x.app-router.api :as router]))



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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-my-props
  ; @return (map)
  ;  {:at-home? (boolean)
  ;   :client-routes (map)
  ;   :router-data (map)}
  [db _]
  {; A kliens-oldali router állapotát leíró feliratkozások:
   :client-routes (r router/get-client-routes db)
   :at-home?      (r router/at-home?          db)
   :router-data   (r router/get-router-data   db)})

(a/subscribe [:router/at-home?])
(a/subscribe [:router/get-router-data])

(defn- get-your-props
  ; @return (map)
  ;  {:route-id (keyword)
  ;   :route-path (string)
  ;   :route-string (string)
  ;   :route-template (string)
  ;   :route-path-params (map)
  ;   :route-path-param (string)
  ;   :route-path-param? (string)
  ;   :route-query-params (map)
  ;   :route-query-param (string)
  ;   :route-query-param? (string)
  ;   :route-fragment (string)
  ;   :route-parent (string)}
  [db _]
  {; Az aktuális útvonal tulajdonságaival visszatérő feliratkozások:
   :route-string       (r router/get-current-route-string       db)
   :route-id           (r router/get-current-route-id           db)
   :route-path         (r router/get-current-route-path         db)
   :route-template     (r router/get-current-route-template     db)
   :route-path-params  (r router/get-current-route-path-params  db)
   :route-path-param   (r router/get-current-route-path-param   db :my-param)
   :route-path-param?  (r router/current-route-path-param?      db :my-param "My value")
   :route-query-params (r router/get-current-route-query-params db)
   :route-query-param  (r router/get-current-route-query-param  db :my-param)
   :route-query-param? (r router/current-route-query-param?     db :my-param "My value")
   :route-fragment     (r router/get-current-route-fragment     db)
   :route-parent       (r router/get-current-route-parent       db)})

(a/subscribe [:router/get-current-route-string])
(a/subscribe [:router/get-current-route-id])
(a/subscribe [:router/get-current-route-path])
(a/subscribe [:router/get-current-route-template])
(a/subscribe [:router/get-current-route-path-params])
(a/subscribe [:router/get-current-route-path-param   :my-param])
(a/subscribe [:router/current-route-path-param?      :my-param "My value"])
(a/subscribe [:router/get-current-route-query-params])
(a/subscribe [:router/get-current-route-query-param  :my-param])
(a/subscribe [:router/current-route-query-param?     :my-param "My value"])
(a/subscribe [:router/get-current-route-fragment])
(a/subscribe [:router/get-current-route-parent])
