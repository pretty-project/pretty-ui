
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.sample
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [pathom.api                            :as pathom]))



;; -- A :debug resolver használata --------------------------------------------
;; ----------------------------------------------------------------------------

; A :debug resolver függvény teljes query vektort kiírja a szerveri-oldali konzolra!
;
; Pl.: [:debug `(:get-my-resolver ~{...})]



;; -- Resolver függvények használata ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-my-resolver-f
  [env resolver-props]
  (let [my-request (pathom/env->request env)
        my-params  (pathom/env->params  env)
        my-param   (pathom/env->param   env :my-param)]
       (str "My return value")))

(defresolver get-my-resolver
             [env resolver-props]
             {:get-my-resolver (get-my-resolver-f env resolver-props)})



;; -- Mutation függvények használata ------------------------------------------
;; ----------------------------------------------------------------------------

(defn do-your-mutation-f
  [env mutation-props]
  (str "Your return value"))

(defmutation do-your-mutation!
             [env mutation-props]
             {::pathom.co/op-name 'do-your-mutation!}
             (do-your-mutation-f env mutation-props))



;; -- Függvények regisztrálása ------------------------------------------------
;; ----------------------------------------------------------------------------

(pathom/reg-handler!  ::handler  [get-my-resolver])
(pathom/reg-handlers! ::handlers [do-your-mutation!])



;; -- Szerver-válasz kliens-oldali címzése ------------------------------------
;; ----------------------------------------------------------------------------

; Az egy HTTP/AJAX lekérésen belüli különböző Pathom resolver és mutation
; függvények visszatérési értékeiben (amennyiben azok térkép adatok) lehetséges
; Re-Frame adatbázis címeket elhelyezni, amely címek alapján a kliens-oldal
; a szervertől kapott adatokat eltárolja a kliens-oldali adatbázisban.

(defresolver target-my-return-data
             [env resolver-props]
             {:target-my-return-data {:my-item            "My value"
                                      :your-item          "Your value"
                                      :pathom/target-path [:my-path]}})
