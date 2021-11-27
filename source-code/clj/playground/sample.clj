
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.27
; Description:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.sample
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
                ; Ha az x.project-config.edn fájlban az :app-home értéke pl. "/my-app"
                ; akkor a "/my-app/sample" útvonalon érheted el a hozzáadott útvonalat,
                ; mivel a {:restricted? true} beállítás használatával a rendszer
                ; applikáció-útvonalként fogja azt kezelni. 
                ; Igen, a {:restricted? false} beállítás használatával webhely-útvonalként kezelné.
  {:on-app-boot [:router/add-route! ::route
                                    {:client-event   [:sample/load!]
                                     :route-template "/sample"
                                     :restricted?    true}]})
