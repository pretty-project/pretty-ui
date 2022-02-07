
(ns pathom.register
    (:require [com.wsscode.pathom3.connect.indexes :as pathom.ci]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A Pathom handlereket lehetseges handler fuggvenyenkent illetve handler
; fuggvenycsoportonkent regisztralni a reg-handler es reg-handlers fuggvenyekkel.
; Igy az esetlegesen hasznalaton kivuli modulok es nevterek handler fuggvenyei
; nem regisztralodnak a Pathom kornyezetbe.
;
; Az egyes handler fuggvenyek es handler fuggvenycsoportok egyedi azonositoval
; kerulnek regisztralasra. Igy biztosithato, hogy a modositott forrasfajlok
; wrap-reload eszkoz altali ujratoltesekor ne regisztraljak tobbszor ugyanazt
; a handler fuggvenyt vagy fuggvenycsoportot a Pathom kornyezetbe.



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (vector)
(def HANDLERS    (atom {}))

; @atom (map)
(def ENVIRONMENT (atom {}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- reset-environment!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [handlers    (-> HANDLERS deref vals)
        registry    [handlers]
        environment (pathom.ci/register registry)]
       (reset! ENVIRONMENT environment)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-handler!
  ; @param (keyword) handler-id
  ; @param (handler function) handler-f
  ;
  ; @usage
  ;  (pco/defmutation do-something! [env] ...)
  ;  (pathom/reg-handler! ::handler do-something!)
  [handler-id handler-f]
  (swap! HANDLERS assoc handler-id handler-f)
  ; Minden handler-függvény regisztrálás után újraépíti a Pathom környezetet,
  ; így biztositva, hogy az egyes forrásfájlok wrap-reload eszköz általi újratöltésekor
  ; újra lefutó reg-handler! függvények regisztrálhassák az esetlegesen megváltozott
  ; handler-függvényeket.
  (reset-environment!))

(defn reg-handlers!
  ; @param (keyword) handlers-id
  ; @param (handler functions in vector) handler-fs
  ;
  ; @usage
  ;  (pco/defmutation do-something! [env] ...)
  ;  (pco/defmutation do-anything!  [env] ...)
  ;  (def HANDLERS [do-something! do-anything!])
  ;  (pathom/reg-handlers! ::handlers HANDLERS)
  [handlers-id handler-fs]
  (swap! HANDLERS assoc handlers-id handler-fs)
  ; Minden handler-függvénycsoport regisztrálás után újraépíti a Pathom környezetet,
  ; így biztositva, hogy az egyes forrásfájlok wrap-reload eszköz általi újratöltésekor
  ; újra lefutó reg-handlers! függvények regisztrálhassák az esetlegesen megváltozott
  ; handler-függvénycsoportokat.
  (reset-environment!))
