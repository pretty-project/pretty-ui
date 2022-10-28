
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.sample
    (:require [x.boot-loader.api :as x.boot-loader]))



;; -- A start-server! függvény használata alapbeállításokkal ------------------
;; ----------------------------------------------------------------------------

(defn start-my-server!
  []
  (x.boot-loader/start-server! {}))



;; -- A start-server! függvény használata egyéni beállításokkal ---------------
;; ----------------------------------------------------------------------------

; A start-server! függvény számára opcionálisan átadható ...
; ... a dev-mode? tulajdonság, ami a monoset a fejlesztői eszközeinek ki/be kapcsolásához használ.
; ... a join? tulajdonság, ami (?) ...
; ... a port tulajdonság, amivel beállítható, hogy az applikació a szerver melyik
;     portján legyen elérhető.
;     Alapbeállításként az environment/x.server-config.edn fájlban beállított
;     értéket használja.
(defn start-my-server!
  []
  (x.boot-loader/start-server! {:dev-mode? true
                                :join?     true
                                :port      420}))
