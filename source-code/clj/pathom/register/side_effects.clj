
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.register.side-effects
    (:require [pathom.register.helpers :as register.helpers]
              [pathom.register.state   :as register.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-handler!
  ; @param (keyword) handler-id
  ; @param (handler function) handler-f
  ;
  ; @usage
  ;  (pco/defmutation my-mutation [env] ...)
  ;  (pathom/reg-handler! ::handler my-mutation)
  [handler-id handler-f]
  (swap! register.state/HANDLERS assoc handler-id handler-f)
  ; Minden handler-függvény regisztrálás után újraépíti a Pathom környezetet,
  ; így biztositva, hogy az egyes forrásfájlok wrap-reload eszköz általi újratöltésekor
  ; újra lefutó reg-handler! függvények regisztrálhassák az esetlegesen megváltozott
  ; handler-függvényeket.
  (register.helpers/reset-environment!))

(defn reg-handlers!
  ; @param (keyword) handlers-id
  ; @param (handler functions in vector) handler-fs
  ;
  ; @usage
  ;  (pco/defmutation my-mutation   [env] ...)
  ;  (pco/defmutation your-mutation [env] ...)
  ;  (def HANDLERS [my-mutation your-mutation])
  ;  (pathom/reg-handlers! ::handlers HANDLERS)
  [handlers-id handler-fs]
  (swap! register.state/HANDLERS assoc handlers-id handler-fs)
  ; Minden handler-függvénycsoport regisztrálás után újraépíti a Pathom környezetet,
  ; így biztositva, hogy az egyes forrásfájlok wrap-reload eszköz általi újratöltésekor
  ; újra lefutó reg-handlers! függvények regisztrálhassák az esetlegesen megváltozott
  ; handler-függvénycsoportokat.
  (register.helpers/reset-environment!))
