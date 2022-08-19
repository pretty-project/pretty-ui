
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
