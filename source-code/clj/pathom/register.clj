
(ns pathom.register
    (:require [mid-fruits.vector :as vector]
              [logger.api        :as logger]
              [x.server-core.api :as a :refer [r]]
              [x.server-db.api   :as db]
              [com.wsscode.pathom3.connect.indexes :as pathom.ci]))



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



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;
; (ns my-handlers
;     (:require [pathom.api         :as pathom :refer [ENVIRONMENT]]
;               [server-fruits.http :as http]))
;
; (defmutation         do-something! [env] ...)
; (pathom/reg-handler! do-something!)
;
; (defresolver get-anything [env] ...)
; (defmutation do-anything! [env] ...)
; (def HANDLERS [get-anything do-anything!])
; (pathom/reg-handlers! HANDLERS)

; @usage
;
; (defn process-query!
;   [request]
;   (let [environment (assoc @ENVIRONMENT :request request)]
;         query       (http/request->param request :query)
;         result (pathom/process-query! (param environment)
;                                       (pathom/read-query query)]
;        (http/map-wrap {:body {...}})))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def REG-LOG-FILENAME "pathom.handler-regs.log")



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
  (let [handlers    (vals (deref HANDLERS))
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
  ;  (pco/defmutation     do-something! [env] ...)
  ;  (pathom/reg-handler! do-something!)
  [handler-id handler-f]
  (logger/write! REG-LOG-FILENAME (str "Registrating handler: " handler-id))
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
  ;  (pco/defmutation     do-something! [env] ...)
  ;  (pco/defmutation     do-anything! [env] ...)
  ;  (def HANDLERS [do-something! do-anything!])
  ;  (pathom/reg-handlers! HANDLERS)
  [handlers-id handler-fs]
  (logger/write! REG-LOG-FILENAME (str "Registrating handlers: " handlers-id))
  (swap! HANDLERS assoc handlers-id handler-fs)
  ; Minden handler-függvénycsoport regisztrálás után újraépíti a Pathom környezetet,
  ; így biztositva, hogy az egyes forrásfájlok wrap-reload eszköz általi újratöltésekor
  ; újra lefutó reg-handlers! függvények regisztrálhassák az esetlegesen megváltozott
  ; handler-függvénycsoportokat.
  (reset-environment!))
